package kr.cosine.parkour.registry

import kr.cosine.parkour.data.ParkourRankingHolder
import kr.hqservice.framework.global.core.component.Bean

@Bean
class ParkourRankingHolderRegistry {
    private val rankingHolderMap = mutableMapOf<String, ParkourRankingHolder>()

    @Transient
    var isChanged = false

    fun restore(parkourRankingHolderRegistry: ParkourRankingHolderRegistry) {
        rankingHolderMap.clear()
        rankingHolderMap.putAll(parkourRankingHolderRegistry.rankingHolderMap)
    }

    fun getRankingHolder(key: String): ParkourRankingHolder {
        return rankingHolderMap.computeIfAbsent(key) {
            ParkourRankingHolder()
        }
    }
}