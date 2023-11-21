package kr.cosine.parkour.announce

import org.bukkit.entity.Player

data class ParkourAnnounce(
    val sound: ParkourSound,
    val chat: ParkourChat,
    val title: ParkourTitle
) {

    fun announce(player: Player) {
        sound.playSound(player)
        chat.sendMessage(player)
        title.sendTitle(player)
    }
}