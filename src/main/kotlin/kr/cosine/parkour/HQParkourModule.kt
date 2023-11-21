package kr.cosine.parkour

import kr.cosine.parkour.config.ParkourConfig
import kr.cosine.parkour.config.SettingConfig
import kr.cosine.parkour.scheduler.ParkourSaveScheduler
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Component
import kr.hqservice.framework.global.core.component.HQModule

@Component
class HQParkourModule(
    private val plugin: HQBukkitPlugin,
    private val settingConfig: SettingConfig,
    private val parkourConfig: ParkourConfig
) : HQModule {

    override fun onEnable() {
        settingConfig.load()
        parkourConfig.load()
        ParkourSaveScheduler(parkourConfig).runTaskTimerAsynchronously(plugin, 3600, 3600)
    }

    override fun onDisable() {
        parkourConfig.save()
    }
}