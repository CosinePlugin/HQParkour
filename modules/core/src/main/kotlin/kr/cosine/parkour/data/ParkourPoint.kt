package kr.cosine.parkour.data

class ParkourPoint() {

    constructor(
        pointLocation: PointLocation
    ) : this() {
        pointLocationMap[1] = pointLocation
    }

    private val pointLocationMap = mutableMapOf<Int, PointLocation>()

    fun getPointLocation(order: Int = 1): PointLocation {
        return pointLocationMap[order] ?: throw IllegalArgumentException()
    }

    fun setPointLocation(order: Int, pointLocation: PointLocation) {
        pointLocationMap[order] = pointLocation
    }
}