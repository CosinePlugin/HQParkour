package kr.cosine.parkour.config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.cosine.parkour.data.Parkour
import kr.cosine.parkour.data.toPointLocation
import kr.cosine.parkour.enums.Point
import kr.cosine.parkour.registry.ParkourRegistry
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.logging.Logger

@Bean
class ParkourConfig(
    plugin: Plugin,
    private val logger: Logger,
    private val parkourRegistry: ParkourRegistry
) {

    private val file = File(plugin.dataFolder, "parkours.yml")
    private val config = YamlConfiguration.loadConfiguration(file)

    fun load() {
        config.getKeys(false).forEach rootForEach@ { key ->
            config.getConfigurationSection(key)?.apply {
                val reward = getItemStack("reward")?.clone()
                val parkour = Parkour(key, reward)
                getConfigurationSection("point")?.apply {
                    getKeys(false).forEach pointForEach@ { pointText ->
                        val point = Point.getPoint(pointText) ?: run {
                            logger.warning("$key.point 섹션의 ${pointText}은(는) 존재하지 않는 Point입니다.")
                            return@pointForEach
                        }
                        getConfigurationSection(pointText)?.apply {
                            getKeys(false).forEach { orderText ->
                                val order = orderText.toIntOrNull() ?: run {
                                    logger.warning("$key.point.$pointText 섹션의 ${orderText}은(는) 숫자가 아닙니다.")
                                    return@forEach
                                }
                                val pointLocation = getString(orderText)?.toPointLocation() ?: run {
                                    logger.warning("$key.point.$pointText.${orderText}의 값을 PointLocation으로 변환하지 못했습니다.")
                                    return@forEach
                                }
                                parkour.setParkourPointLocation(point, order, pointLocation)
                            }
                        }
                    }
                }
                parkour.isChanged = false
                parkourRegistry.setParkour(key, parkour)
            }
        }
    }

    fun save() {
        val changedParkour = parkourRegistry.getParkourMap().filter { it.value.isChanged }
        if (changedParkour.isNotEmpty()) {
            changedParkour.forEach { (key, parkour) ->
                config.set("$key.reward", parkour.getReward())
                parkour.getParkourPointMap().forEach { (point, parkourPoint) ->
                    parkourPoint.getPointLocationMap().forEach { (order, pointLocation) ->
                        config.set("$key.point.${point.name}.$order", pointLocation.toString())
                    }
                }
                parkour.isChanged = false
            }
            config.save(file)
        }
    }

    suspend fun delete(key: String) {
        parkourRegistry.removeParkour(key)
        withContext(Dispatchers.IO) {
            config.set(key, null)
            config.save(file)
        }
    }
}