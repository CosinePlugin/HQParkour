package kr.cosine.parkour.data

import kr.cosine.parkour.enums.Point

class Parkour(
    val key: String
) {

    constructor(
        key: String,
        startPointLocation: PointLocation,
        endPointLocation: PointLocation,
        middlePointMapLocation: MutableMap<Int, PointLocation>
    ): this(key) {
        parkourPointMapType[Point.START] = ParkourPoint(startPointLocation)
        parkourPointMapType[Point.END] = ParkourPoint(endPointLocation)
        val middleParkourPoint = ParkourPoint()
        middlePointMapLocation.forEach { (order, lazyLocation) ->
            middleParkourPoint.setPointLocation(order, lazyLocation)
        }
        parkourPointMapType[Point.MIDDLE] = middleParkourPoint
    }

    private val parkourPointMapType = mutableMapOf<Point, ParkourPoint>()

    var isChanged = false

    fun findParkourPoint(point: Point): ParkourPoint? = parkourPointMapType[point]

    fun getParkourPoint(point: Point): ParkourPoint = findParkourPoint(point) ?: throw IllegalArgumentException()

    fun setParkourPoint(point: Point, order: Int, pointLocation: PointLocation) {
        parkourPointMapType.computeIfAbsent(point) {
            ParkourPoint()
        }.setPointLocation(order, pointLocation)
        isChanged = true
    }
}