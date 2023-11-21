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
        z: Int,
        yaw: Float,
        pitch: Float
    ): this(worldName, x.toDouble(), y.toDouble(), z.toDouble(), yaw, pitch)

    fun isEqual(location: Location): Boolean {
        val world = location.world ?: return false
        return world.name == worldName && location.blockX == x.toInt() && location.blockY == y.toInt() && location.blockZ == z.toInt()
    }

    fun teleport(player: Player, plusX: Double = 0.5, plusY: Double = 1.0, plusZ: Double = 0.5) {
        val world = Bukkit.getWorld(worldName) ?: return
        val location = Location(world, x + plusX, y + plusY, z + plusZ, yaw, pitch)
        player.teleport(location)
    }

    fun format(): String {
        return "world: §7$worldName§f, x: §7$x§f, y: §7$y§f, z: §7$z, yaw: §7${yaw.slice()}, pitch: §7${pitch.slice()}"
    }

    private fun Float.slice(): String {
        return toString().run { if (length > 4) substring(0..4) else this }
    }

    override fun toString(): String {
        return "$worldName, $x, $y, $z, $yaw, $pitch"
    }
}

internal fun String.toPointLocation(): PointLocation? {
    return runCatching {
        val split = split(", ")
        val worldName = split[0]
        val x = split[1].toDouble()
        val y = split[2].toDouble()
        val z = split[3].toDouble()
        val yaw = split[4].toFloat()
        val pitch = split[5].toFloat()
        PointLocation(worldName, x, y, z, yaw, pitch)
    }.getOrNull()
}

internal fun Location.toPointLocation(): PointLocation? {
    val world = world ?: return null
    val worldName = world.name
    return PointLocation(worldName, blockX, blockY, blockZ, yaw, pitch)
}