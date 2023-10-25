package kr.cosine.parkour.listener

import kr.cosine.parkour.service.ParkourService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.PlayerMoveEvent

@Listener
class ParkourListener(
    private val parkourService: ParkourService
) {

    @Subscribe
    fun onMove(event: PlayerMoveEvent) {
        parkourService.stepChecker(event.player)
    }
}