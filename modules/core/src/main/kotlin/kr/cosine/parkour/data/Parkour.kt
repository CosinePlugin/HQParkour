package kr.cosine.parkour.data

import kr.cosine.parkour.enums.Point
import org.bukkit.inventory.ItemStack
import java.util.UUID

class Parkour(
    val key: String,
    private var reward: ItemStack? = null
) {

    constructor(
        key: String,
        reward: ItemStack,
        waitPoingLocation: PointLocation,
        startPointLocation: PointLocation,
        endPointLocation: PointLocation,
        middlePointMapLocation: MutableMap<Int, PointLocation>
    ): this(key, reward) {
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

    fun getReward(): ItemStack? = reward?.clone()

    fun setReward(reward: ItemStack) {
        this.reward = reward.clone()
    }

    fun findParkourPoint(point: Point): ParkourPoint? = parkourPointMap[point]

    fun getParkourPoint(point: Point): ParkourPoint = findParkourPoint(point) ?: throw IllegalArgumentException()

    fun setParkourPointLocation(point: Point, order: Int, pointLocation: PointLocation) {
        parkourPointMap.computeIfAbsent(point) {
            ParkourPoint()
        }.setPointLocation(order, pointLocation)
        isChanged = true
    }

    fun getParkourPointMap(): Map<Point, ParkourPoint> = parkourPointMap

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