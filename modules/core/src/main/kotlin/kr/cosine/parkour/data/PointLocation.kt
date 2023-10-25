package kr.cosine.parkour.data

import org.bukkit.Location

data class PointLocation(
    val worldName: String,
    val x: Int,
    val y: Int,
    val z: Int
) {

    fun isEqual(location: Location): Boolean {
        val world = location.world ?: return false
        return world.name == worldName && location.blockX == x && location.blockY == y && location.blockZ == z
    }
}

internal fun Location.toPointLocation(): PointLocation? {
    val world = world ?: return null
    return PointLocation(world.name, blockX, blockY, blockZ)
}