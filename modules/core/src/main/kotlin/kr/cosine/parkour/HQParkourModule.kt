package kr.cosine.parkour

import kr.cosine.parkour.config.ParkourConfig
import kr.cosine.parkour.scheduler.ParkourSaveScheduler
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Component
import kr.hqservice.framework.global.core.component.HQModule

@Component
class HQParkourModule(
    private val plugin: HQBukkitPlugin,
    private val parkourConfig: ParkourConfig
) : HQModule {

    internal companion object {
        val prefix = "<g:c3eb34>§l[Parkour]</g:0ae9f5>§f".colorize()
    }

    override fun onEnable() {
        parkourConfig.load()
        ParkourSaveScheduler(parkourConfig).runTaskTimerAsynchronously(plugin, 3600, 3600)
    }

    override fun onDisable() {
        parkourConfig.save()
    }
}