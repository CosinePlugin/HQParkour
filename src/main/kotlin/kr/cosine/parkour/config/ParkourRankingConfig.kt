package kr.cosine.parkour.config

import com.google.gson.GsonBuilder
import kr.cosine.parkour.registry.ParkourRankingHolderRegistry
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.plugin.Plugin
import java.io.File

@Bean
class ParkourRankingConfig(
    plugin: Plugin,
    private val parkourRankingHolderRegistry: ParkourRankingHolderRegistry
) {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create()

    private val file = File(plugin.dataFolder, "parkour-ranking.json")

    fun load() {
        if (!file.exists()) return
        val parkourRankingHolderRegistry = gson.fromJson(file.bufferedReader(), ParkourRankingHolderRegistry::class.java)
        this.parkourRankingHolderRegistry.restore(parkourRankingHolderRegistry)
    }

    fun save() {
        if (parkourRankingHolderRegistry.isChanged) {
            val json = gson.toJson(parkourRankingHolderRegistry)
            file.bufferedWriter().use {
                it.appendLine(json)
                it.flush()
            }
            parkourRankingHolderRegistry.isChanged = false
        }
    }
}