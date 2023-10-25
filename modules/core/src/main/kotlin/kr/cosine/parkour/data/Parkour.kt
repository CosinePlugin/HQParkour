package kr.cosine.parkour.data

import kr.cosine.parkour.enums.Point
import java.util.UUID

class Parkour(
    val key: String
) {

    constructor(
        key: String,
        waitPoingLocation: PointLocation,
        startPointLocation: PointLocation,
        endPointLocation: PointLocation,
        middlePointMapLocation: MutableMap<Int, PointLocation>
    ): this(key) {
        parkourPointMap[Point.WAIT] = ParkourPoint(waitPoingLocation)
        parkourPointMap[Point.START] = ParkourPoint(startPointLocation)
        parkourPointMap[Point.END] = ParkourPoint(endPointLocation)
        val middleParkourPoint = ParkourPoint()
        middlePointMapLocation.forEach { (order, lazyLocation) ->
            middleParkourPoint.setPointLocation(order, lazyLocation)
        }
        parkourPointMap[Point.MIDDLE] = middleParkourPoint
    }

    private val parkourPointMap = mutableMapOf<Point, ParkourPoint>()

    private val parkourPlayerMap = mutableMapOf<UUID, ParkourPlayer>()

    var isChanged = false

    fun findParkourPoint(point: Point): ParkourPoint? = parkourPointMap[point]

    fun getParkourPoint(point: Point): ParkourPoint = findParkourPoint(point) ?: throw IllegalArgumentException()

    fun setParkourPoint(point: Point, order: Int, pointLocation: PointLocation) {
        parkourPointMap.computeIfAbsent(point) {
            ParkourPoint()
        }.setPointLocation(order, pointLocation)
        isChanged = true
    }

    fun isParkorPlayer(playerUniqueId: UUID): Boolean = parkourPlayerMap.containsKey(playerUniqueId)

    fun findParkourPlayer(playerUniqueId: UUID): ParkourPlayer? = parkourPlayerMap[playerUniqueId]

    fun getParkourPlayer(playerUniqueId: UUID): ParkourPlayer = findParkourPlayer(playerUniqueId) ?: throw IllegalArgumentException()

    fun setParkourPlayer(playerUniqueId: UUID): ParkourPlayer {
        val parkourPlayer = ParkourPlayer()
        parkourPlayerMap[playerUniqueId] = parkourPlayer
        return parkourPlayer
    }

    fun removeParkourPlayer(playerUniqueId: UUID) {
        parkourPlayerMap.remove(playerUniqueId)
    }
}