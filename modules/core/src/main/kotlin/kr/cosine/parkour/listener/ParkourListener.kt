package kr.cosine.parkour.listener

import kr.cosine.parkour.service.ParkourService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent

@Listener
class ParkourListener(
    private val parkourService: ParkourService
) {

    @Subscribe
    fun onMove(event: PlayerMoveEvent) {
        /*val fromLocation = event.from
        val toLocation = event.to ?: return
        if (fromLocation.block == toLocation.block) return*/
        parkourService.stepChecker(event.player)
    }

    @Subscribe
    fun onQuit(event: PlayerQuitEvent) {
        parkourService.removeFromParkourPlayer(event.player)
    }
}