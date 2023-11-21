package kr.cosine.parkour.data

import org.bukkit.entity.Player

data class Message(
    private val message: String
) {

    fun sendMessage(player: Player) {
        if (message.isNotEmpty()) {
            player.sendMessage(message)
        }
    }
}