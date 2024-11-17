package kr.cosine.parkour.placeholder.module

import kr.cosine.parkour.placeholder.expansion.ParkourRankingExpansion
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import org.bukkit.plugin.PluginManager

@Module
class ExpansionModule(
    private val pluginManager: PluginManager,
    private val parkourRankingExpansion: ParkourRankingExpansion
) {
    @Setup
    fun setup() {
        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            parkourRankingExpansion.register()
        }
    }
}