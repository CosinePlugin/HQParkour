package kr.cosine.parkour.config

import kr.cosine.parkour.registry.ParkourRankingRegistry
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.UUID

@Bean
class ParkourRankingConfig(
    plugin: Plugin,
    private val parkourRankingRegistry: ParkourRankingRegistry
) {

    private val file = File(plugin.dataFolder, "parkour-ranking.yml")
    private val config = YamlConfiguration.loadConfiguration(file)

    fun load() {
        if (!file.exists()) return
        config.getConfigurationSection("ranking")?.apply {
            getKeys(false).forEach { uniqueIdText ->
                val uniqueId = UUID.fromString(uniqueIdText)
                val time = getLong(uniqueIdText)
                parkourRankingRegistry.setRanking(uniqueId, time)
            }
        }
    }

    fun save() {
        parkourRankingRegistry.apply {
            if (isChanged) {
                getRankingMap().toMap().forEach { (uniqueId, time) ->
                    config.set("ranking.$uniqueId", time)
                }
                config.save(file)
                isChanged = false
            }
        }
    }
}