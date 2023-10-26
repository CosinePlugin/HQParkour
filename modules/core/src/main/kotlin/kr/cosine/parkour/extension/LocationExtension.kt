package kr.cosine.parkour.extension

import org.bukkit.block.BlockFace
import org.bukkit.entity.Player

internal val Player.downBlockLocation get() = location.block.getRelative(BlockFace.DOWN).location