package kr.cosine.parkour.registry

import kr.cosine.parkour.announce.ParkourAnnounce
import kr.cosine.parkour.enums.Announce
import kr.hqservice.framework.global.core.component.Bean

@Bean
class AnnounceRegistry {

    private val announceMap = mutableMapOf<Announce, ParkourAnnounce>()

    fun findParkourAnnounce(announce: Announce): ParkourAnnounce? = announceMap[announce]

    fun getParkourAnnounce(announce: Announce): ParkourAnnounce = findParkourAnnounce(announce) ?: throw IllegalArgumentException()

    fun setParkourAnnounce(announce: Announce, parkourAnnounce: ParkourAnnounce) {
        announceMap[announce] = parkourAnnounce
    }

    internal fun clear() {
        announceMap.clear()
    }
}