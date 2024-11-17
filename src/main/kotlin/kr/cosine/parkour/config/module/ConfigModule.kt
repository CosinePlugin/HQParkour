package kr.cosine.parkour.config.module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kr.cosine.parkour.config.ParkourConfig
import kr.cosine.parkour.config.ParkourRankingConfig
import kr.cosine.parkour.config.SettingConfig
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import kr.hqservice.framework.bukkit.core.component.module.Teardown
import kr.hqservice.framework.bukkit.core.coroutine.bukkitDelay
import kr.hqservice.framework.bukkit.core.coroutine.element.TeardownOptionCoroutineContextElement

@Module
class ConfigModule(
    private val plugin: HQBukkitPlugin,
    private val settingConfig: SettingConfig,
    private val parkourConfig: ParkourConfig,
    private val parkourRankingConfig: ParkourRankingConfig
) {
    @Setup
    fun setup() {
        settingConfig.load()
        parkourConfig.load()
        parkourRankingConfig.load()

        startAutoSave(3600) {
            parkourRankingConfig.save()
        }
        startAutoSave(6000) {
            parkourRankingConfig.save()
        }
    }

    private fun startAutoSave(delay: Long, block: () -> Unit) {
        plugin.launch(Dispatchers.IO + TeardownOptionCoroutineContextElement(true)) {
            while (isActive) {
                bukkitDelay(delay)
                block()
            }
        }
    }

    @Teardown
    fun teardown() {
        parkourConfig.save()
        parkourRankingConfig.save()
    }
}