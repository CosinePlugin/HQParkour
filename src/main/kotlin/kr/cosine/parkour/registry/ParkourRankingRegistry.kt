package kr.cosine.parkour.registry

import kr.hqservice.framework.global.core.component.Bean
import java.util.UUID

@Bean
class ParkourRankingRegistry {

    private val rankingMap = mutableMapOf<UUID, Long>()

    var isChanged = false

    fun findRanking(uniqueId: UUID): Long? = rankingMap[uniqueId]

    fun setRanking(uniqueId: UUID, time: Long) {
        val beforeTime = findRanking(uniqueId)
        if (beforeTime != null && beforeTime < time) return
        rankingMap[uniqueId] = time
        isChanged = true
    }

    fun getRankingMap(): Map<UUID, Long> = rankingMap.toSortedMap(compareBy { rankingMap[it] })
}