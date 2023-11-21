package kr.cosine.parkour.scheduler

import kr.cosine.parkour.config.ParkourRankingConfig
import org.bukkit.scheduler.BukkitRunnable

class ParkourRankingSaveScheduler(
    private val parkourRankingConfig: ParkourRankingConfig
) : BukkitRunnable() {

    override fun run() {
        parkourRankingConfig.save()
    }
}