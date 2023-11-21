package kr.cosine.parkour.registry

import kr.hqservice.framework.global.core.component.Bean

@Bean
class PlaceholderRegistry {

    var recordedNoneMessage = ""
        private set

    var rankingMessage = ""
        private set

    fun setRecordedNoneMessage(recordedNoneMessage: String) {
        this.recordedNoneMessage = recordedNoneMessage
    }

    fun setRankingMessage(rankingMessage: String) {
        this.rankingMessage = rankingMessage
    }
}