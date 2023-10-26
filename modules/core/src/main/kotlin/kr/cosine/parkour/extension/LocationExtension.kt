package kr.cosine.parkour.extension

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player

internal val Player.downBlockLocation get() = location.block.downLocation

internal val Block.downLocation get() = getRelative(BlockFace.DOWN).location