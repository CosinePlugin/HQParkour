package kr.cosine.parkour.data

import org.bukkit.entity.Player

data class ErrorMessage(
    private val message: String
) {

    fun sendMessage(player: Player) {
        player.sendMessage(message)
    }
}