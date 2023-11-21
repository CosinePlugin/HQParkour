package kr.cosine.parkour.expansion

import kr.cosine.parkour.component.ParkourRankingComponent
import kr.cosine.parkour.extension.toKoreanTimeFormat
import kr.cosine.parkour.registry.ParkourRankingRegistry
import kr.cosine.parkour.registry.PlaceholderRegistry
import kr.hqservice.framework.global.core.component.Component
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Server
import org.bukkit.entity.Player
import java.util.*

@Component
class ParkourRankingExpansion(
    private val server: Server,
    private val placeholderRegistry: PlaceholderRegistry,
    private val parokurRankingRegistry: ParkourRankingRegistry
) : PlaceholderExpansion(), ParkourRankingComponent {

    private val recordedNoneMessage get() = placeholderRegistry.recordedNoneMessage

    override fun getIdentifier(): String = "hqparkour"

    override fun getAuthor(): String = "cosine"

    override fun getVersion(): String = "1.0.0"

    override fun onPlaceholderRequest(player: Player, params: String): String? {
        if (params.contains("ranking")) {
            val orderText = params.removePrefix("ranking")
            val order = orderText.toIntOrNull() ?: return null
            val element = parokurRankingRegistry.getRankingMap().entries.elementAtOrNull(order - 1) ?: return recordedNoneMessage
            val name = element.key.getName()
            val time = element.value.toKoreanTimeFormat()
            return placeholderRegistry.rankingMessage.replace("%player%", name).replace("%time%", time)
        }
        return when (params) {
            "time" -> parokurRankingRegistry.findRanking(player.uniqueId)?.toKoreanTimeFormat() ?: recordedNoneMessage
            else -> null
        }
    }

    private fun UUID.getName(): String {
        return server.offlinePlayers.firstOrNull { it.uniqueId == this }?.name ?: this.toString()
    }

    override fun setup() {
        register()
    }
}