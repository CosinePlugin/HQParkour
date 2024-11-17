package kr.cosine.parkour.data

import org.bukkit.entity.Player
import java.util.UUID

data class ParkourRankingHolder(
    private val rankingMap: MutableMap<UUID, Ranking> = mutableMapOf()
) : MutableMap<UUID, ParkourRankingHolder.Ranking> by rankingMap {
    data class Ranking(
        val name: String,
        val time: Long
    )

    fun refresh(player: Player, time: Long): Boolean {
        val playerUniqueId = player.uniqueId
        val beforeRanking = get(playerUniqueId)
        if (beforeRanking != null && beforeRanking.time < time) return false
        val newRanking = beforeRanking?.copy(
            name = player.name,
            time = time
        ) ?: Ranking(player.name, time)
        put(playerUniqueId, newRanking)
        return true
    }
}