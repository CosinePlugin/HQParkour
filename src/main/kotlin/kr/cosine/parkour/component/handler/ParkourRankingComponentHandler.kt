package kr.cosine.parkour.component.handler

import kr.cosine.parkour.component.ParkourRankingComponent
import kr.cosine.parkour.config.ParkourRankingConfig
import kr.cosine.parkour.scheduler.ParkourRankingSaveScheduler
import kr.hqservice.framework.global.core.component.handler.ComponentHandler
import kr.hqservice.framework.global.core.component.handler.HQComponentHandler
import org.bukkit.plugin.Plugin

@ComponentHandler
class ParkourRankingComponentHandler(
    private val plugin: Plugin,
    private val parkourRankingConfig: ParkourRankingConfig
) : HQComponentHandler<ParkourRankingComponent> {

    override fun setup(element: ParkourRankingComponent) {
        if (plugin.server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            element.setup()
        }
        parkourRankingConfig.load()
        ParkourRankingSaveScheduler(parkourRankingConfig).runTaskTimerAsynchronously(plugin, 6000, 6000)
    }

    override fun teardown(element: ParkourRankingComponent) {
        parkourRankingConfig.save()
    }
}