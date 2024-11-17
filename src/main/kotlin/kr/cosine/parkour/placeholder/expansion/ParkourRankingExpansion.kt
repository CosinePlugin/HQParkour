package kr.cosine.parkour.placeholder.expansion

import kr.cosine.parkour.extension.toKoreanTimeFormat
import kr.cosine.parkour.registry.ParkourRankingHolderRegistry
import kr.cosine.parkour.registry.PlaceholderRegistry
import kr.hqservice.framework.global.core.component.Bean
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

@Bean
class ParkourRankingExpansion(
    private val placeholderRegistry: PlaceholderRegistry,
    private val parokurRankingRegistry: ParkourRankingHolderRegistry
) : PlaceholderExpansion() {
    private val recordedNoneMessage get() = placeholderRegistry.recordedNoneMessage

    override fun getIdentifier(): String = "hqparkour"

    override fun getAuthor(): String = "Cosine_A"

    override fun getVersion(): String = "1.1.0"

    // %hqparkour_top_ranking_key% = 유저 전용
    // %hqparkour_ranking_key_1%
    // └ ranking_key_1
    override fun onPlaceholderRequest(player: Player, params: String): String? {
        if (params.contains("top_ranking")) {
            val key = params.removePrefix("top_ranking_")
            val rankingMap = parokurRankingRegistry.getRankingHolder(key)
            return rankingMap[player.uniqueId]?.time?.toKoreanTimeFormat() ?: recordedNoneMessage
        }
        if (params.contains("ranking")) {
            val keyAndOrder = params.removePrefix("ranking_")
            val orderText = keyAndOrder.substringAfterLast("_")
            val order = orderText.toIntOrNull() ?: return null
            val key = keyAndOrder.removeSuffix("_$orderText")

            val rankingMap = parokurRankingRegistry.getRankingHolder(key)
            val sortedRankingMap = rankingMap.toSortedMap(compareBy { rankingMap[it]?.time }).entries

            val element = sortedRankingMap.elementAtOrNull(order - 1) ?: return recordedNoneMessage
            val ranking = element.value
            val time = ranking.time.toKoreanTimeFormat()
            return placeholderRegistry.rankingMessage
                .replace("%player%", ranking.name)
                .replace("%time%", time)
        }
        return null
    }
}