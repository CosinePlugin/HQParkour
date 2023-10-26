package kr.cosine.parkour.listener

import kr.cosine.parkour.extension.downLocation
import kr.cosine.parkour.service.ParkourService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent

@Listener
class ParkourListener(
    private val parkourService: ParkourService
) {

    /*@Subscribe
    fun onMove(event: PlayerMoveEvent) {
        val fromLocation = event.from
        val toLocation = event.to ?: return
        if (fromLocation.x == toLocation.x && fromLocation.y == toLocation.y && fromLocation.z == toLocation.z) return
        parkourService.stepChecker(event.player)
    }*/

    @Subscribe
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action != Action.PHYSICAL) return
        val block = event.clickedBlock ?: return
        if (block.type != Material.STONE_PRESSURE_PLATE) return
        parkourService.stepChecker(event.player, block.downLocation)
    }

    @Subscribe
    fun onQuit(event: PlayerQuitEvent) {
        parkourService.removeFromParkourPlayer(event.player)
    }
}