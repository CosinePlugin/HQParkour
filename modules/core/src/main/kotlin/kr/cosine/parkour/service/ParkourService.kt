package kr.cosine.parkour.service

import kr.cosine.parkour.enums.Point
import kr.cosine.parkour.registry.ParkourRegistry
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.Location
import org.bukkit.entity.Player

@Service
class ParkourService(
    private val parkourRegistry: ParkourRegistry
) {

    fun stepChecker(player: Player, location: Location) {
        stepStartPoint(player, location)
        stepMiddlePoint(player, location)
        stepEndPoint(player, location)
    }

    fun removeFromParkourPlayer(player: Player) {
        parkourRegistry.getParkourValues().forEach {
            it.removeParkourPlayer(player.uniqueId)
        }
    }

    private fun stepStartPoint(player: Player, location: Location) {
        val parkour = parkourRegistry.findParkourByLocation(Point.START, location) ?: return

        val playerUniqueId = player.uniqueId
        if (parkour.isParkorPlayer(playerUniqueId)) return

        player.sendMessage("파쿠르 시작")
        parkour.setParkourPlayer(playerUniqueId).apply {
            startTime = System.currentTimeMillis()
        }
    }

    private fun stepEndPoint(player: Player, location: Location) {
        val parkour = parkourRegistry.findParkourByLocation(Point.END, location) ?: return

        val playerUniqueId = player.uniqueId
        val parkourPlayer = parkour.findParkourPlayer(playerUniqueId) ?: return
        if (parkourPlayer.currentPoint != Point.MIDDLE) return

        val middleParkourPoint = parkour.findParkourPoint(Point.MIDDLE) ?: return
        if (parkourPlayer.currentMiddlePointOrder != middleParkourPoint.getLastPointOrder()) {
            player.sendMessage("MIDDLE 마지막 포인트 안밟음")
            return
        }

        player.sendMessage("§f다 깼음!! §7(걸린 시간: ${System.currentTimeMillis() - parkourPlayer.startTime})")
        parkour.removeParkourPlayer(playerUniqueId)
        parkour.getReward()?.let { player.inventory.addItem(it) }
        parkour.findParkourPoint(Point.WAIT)?.getPointLocation()?.teleport(player)
    }

    private fun stepMiddlePoint(player: Player, location: Location) {
        val parkour = parkourRegistry.findParkourByLocation(Point.MIDDLE, location) ?: return

        val playerUniqueId = player.uniqueId
        val parkourPlayer = parkour.findParkourPlayer(playerUniqueId) ?: return

        val middleParkourPoint = parkour.findParkourPoint(Point.MIDDLE) ?: return
        val steppedPointOrder = middleParkourPoint.getPointOrderByLocation(location) ?: return
        player.sendMessage("${steppedPointOrder}번 중간 지점 밟음")

        when (parkourPlayer.currentPoint) {
            Point.START -> {
                if (steppedPointOrder != 1) {
                    player.sendMessage("MIDDLE 1번째 안밟음")
                    return
                }
                parkourPlayer.currentPoint = Point.MIDDLE
            }
            Point.MIDDLE -> {
                // [O] currentMiddlePointOrder(1) == steppedPointOrder(2) - 1
                // [X] currentMiddlePointOrder(1) =/= steppedPointOrder(3) - 1
                if (steppedPointOrder <= parkourPlayer.currentMiddlePointOrder) {
                    player.sendMessage("이미 밟은 곳임")
                    return
                }
                if (parkourPlayer.currentMiddlePointOrder != steppedPointOrder - 1) {
                    player.sendMessage("이전 포인트 안밟음")
                    return
                }
                parkourPlayer.currentMiddlePointOrder = steppedPointOrder
                player.sendMessage("§7현재 포인트: ${parkourPlayer.currentMiddlePointOrder}")
            }
            else -> {}
        }
    }
}