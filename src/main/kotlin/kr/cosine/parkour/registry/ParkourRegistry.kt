package kr.cosine.parkour.registry

import kr.cosine.parkour.data.Parkour
import kr.cosine.parkour.enums.Point
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.Location
import java.util.UUID

@Bean
class ParkourRegistry {

    private val parkourMap = mutableMapOf<String, Parkour>()

    fun isParkour(key: String): Boolean = parkourMap.containsKey(key)

    fun findParkour(key: String): Parkour? = parkourMap[key]

    fun getParkour(key: String): Parkour = findParkour(key) ?: throw IllegalArgumentException()

    fun setParkour(key: String, parkour: Parkour) {
        parkourMap[key] = parkour
    }

    fun findParkourByPlayer(playerUniqueId: UUID): Parkour? {
        return parkourMap.values.firstOrNull {
            it.isParkorPlayer(playerUniqueId)
        }
    }

    fun findParkourByLocation(point: Point, location: Location): Parkour? {
        return parkourMap.values.firstOrNull {
            it.findParkourPoint(point)?.getPointLocationByLocation(location) != null
        }
    }

    fun getParkourKeys(): List<String> = getParkourMap().keys.toList()

    fun getParkourValues(): List<Parkour> = getParkourMap().values.toList()

    fun getParkourMap(): Map<String, Parkour> = parkourMap

    fun removeParkour(key: String) {
        parkourMap.remove(key)
    }
}