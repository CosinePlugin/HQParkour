package kr.cosine.parkour.event

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class ParkourSuccessEvent(
    who: Player,
    val key: String,
    val reward: ItemStack?,
    val time: Long
) : PlayerEvent(who), Cancellable {

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlers
    }

    private var cancel = false

    override fun isCancelled(): Boolean = cancel

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }

    override fun getHandlers(): HandlerList = getHandlerList()
}