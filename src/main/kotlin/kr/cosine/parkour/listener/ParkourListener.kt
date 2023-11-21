package kr.cosine.parkour.listener

import kr.cosine.parkour.extension.downLocation
import kr.cosine.parkour.service.ParkourService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerQuitEvent

@Listener
class ParkourListener(
    private val parkourService: ParkourService
) {

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

    @Subscribe
    fun onPress(event: PlayerItemHeldEvent) {
        if (parkourService.parkourKeyAction(event.player, event.newSlot)) {
            event.isCancelled = true
        }
    }

    @Subscribe
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        if (parkourService.isCommandUsePrevented(event.player)) {
            event.isCancelled = true
        }
    }
}