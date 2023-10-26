package kr.cosine.parkour.announce

import org.bukkit.entity.Player

data class ParkourTitle(
    private val enabled: Boolean,
    private val title: String,
    private val subTitle: String,
    private val fadeIn: Int,
    private val duration: Int,
    private val fadeOut: Int
) {

    fun sendTitle(player: Player, replaceFunction: (String) -> String = { it }) {
        if (enabled) {
            player.sendTitle(replaceFunction(title), replaceFunction(subTitle), fadeIn, duration, fadeOut)
        }
    }
}