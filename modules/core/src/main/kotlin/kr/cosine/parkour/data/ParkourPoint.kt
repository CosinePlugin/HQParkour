package kr.cosine.parkour.data

import org.bukkit.Location

class ParkourPoint() {

    constructor(
        pointLocation: PointLocation
    ) : this() {
        pointLocationMap[1] = pointLocation
    }

    private val pointLocationMap = mutableMapOf<Int, PointLocation>()

    fun isPointLocation(order: Int): Boolean = pointLocationMap.containsKey(order)

    fun getPointLocation(order: Int = 1): PointLocation {
        return pointLocationMap[order] ?: throw IllegalArgumentException()
    }

    fun setPointLocation(order: Int, pointLocation: PointLocation) {
        pointLocationMap[order] = pointLocation
    }

    fun getPointLocationByLocation(location: Location): PointLocation? {
        return pointLocationMap.values.firstOrNull { it.isEqual(location) }
    }

    fun getPointOrderByLocation(location: Location): Int? {
        return pointLocationMap.entries.firstOrNull { it.value.isEqual(location) }?.key
    }

    fun getPointLocationMap(): Map<Int, PointLocation> = pointLocationMap

    fun removePointLocation(order: Int) {
        pointLocationMap.remove(order)
    }

    fun getLastPointOrder(): Int = pointLocationMap.keys.max()
}