package kr.cosine.parkour.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

data class PointLocation(
    val worldName: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = 0f,
    val pitch: Float = 0f
) {

    constructor(
        worldName: String,
        x: Int,
        y: Int,
        z: Int
    ): this(worldName, x.toDouble(), y.toDouble(), z.toDouble())

    fun isEqual(location: Location): Boolean {
        val world = location.world ?: return false
        return world.name == worldName && location.blockX == x.toInt() && location.blockY == y.toInt() && location.blockZ == z.toInt()
    }

    fun teleport(player: Player) {
        val world = Bukkit.getWorld(worldName) ?: return
        val location = Location(world, x, y, z, yaw, pitch)
        player.teleport(location)
    }
}

internal fun Location.toPointLocation(): PointLocation? {
    val world = world ?: return null
    return PointLocation(world.name, blockX, blockY, blockZ)
}