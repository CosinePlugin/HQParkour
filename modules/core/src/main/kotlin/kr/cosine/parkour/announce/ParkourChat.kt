package kr.cosine.parkour.announce

import org.bukkit.entity.Player

data class ParkourChat(
    private val enabled: Boolean,
    private val message: String
) {

    fun sendMessage(player: Player, replaceFunction: (String) -> String = { it }) {
        if (enabled) {
            player.sendMessage(replaceFunction(message))
        }
    }
}