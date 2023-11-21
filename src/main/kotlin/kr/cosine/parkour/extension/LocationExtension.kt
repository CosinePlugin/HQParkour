package kr.cosine.parkour.extension

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player

internal val Player.downLocation get() = location.subtract(0.0, 1.0, 0.0)

internal val Location.upBlockLocation get() = block.getRelative(BlockFace.UP).location

internal val Block.downLocation get() = getRelative(BlockFace.DOWN).location