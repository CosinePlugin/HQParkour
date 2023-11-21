package kr.cosine.parkour.scheduler

import kr.cosine.parkour.config.ParkourConfig
import org.bukkit.scheduler.BukkitRunnable

class ParkourSaveScheduler(
    private val parkourConfig: ParkourConfig
) : BukkitRunnable() {

    override fun run() {
        parkourConfig.save()
    }
}