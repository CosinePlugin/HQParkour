package kr.cosine.parkour.registry

import kr.cosine.parkour.data.Parkour
import kr.cosine.parkour.enums.Point
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.Location

@Bean
class ParkourRegistry {

    private val parkourMap = mutableMapOf<String, Parkour>()

    fun findParkour(key: String): Parkour? = parkourMap[key]

    fun getParkour(key: String): Parkour = findParkour(key) ?: throw IllegalArgumentException()

    fun setParkour(key: String, parkour: Parkour) {
        parkourMap[key] = parkour
    }

    fun getParkourByLocation(point: Point, order: Int, location: Location): Parkour? {
        return parkourMap.values.firstOrNull {
            it.findParkourPoint(point)?.getPointLocation(order)?.isEqual(location) == true
        }
    }
}