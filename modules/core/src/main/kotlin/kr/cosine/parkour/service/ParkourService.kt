package kr.cosine.parkour.service

import kr.cosine.parkour.enums.Point
import kr.cosine.parkour.registry.ParkourRegistry
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player

@Service
class ParkourService(
    private val parkourRegistry: ParkourRegistry
) {

    fun stepChecker(player: Player) {
        stepStartPoint(player)
        stepMiddlePoint(player)
        stepEndPoint(player)
    }

    private fun stepStartPoint(player: Player) {
        val parkour = parkourRegistry.findParkourByLocation(Point.START, 1, player.downLocation) ?: return

        val playerUniqueId = player.uniqueId
        if (parkour.isParkorPlayer(playerUniqueId)) return

        parkour.setParkourPlayer(playerUniqueId).apply {
            startTime = System.currentTimeMillis()
        }
    }

    private fun stepMiddlePoint(player: Player) {
        val playerDownLocation = player.downLocation
        val parkour = parkourRegistry.findParkourByLocation(Point.END, 1, playerDownLocation) ?: return

        val playerUniqueId = player.uniqueId
        val parkourPlayer = parkour.findParkourPlayer(playerUniqueId) ?: return

        val middleParkourPoint = parkour.getParkourPoint(Point.MIDDLE)
        val steppedPointOrder = middleParkourPoint.getPointOrderByLocation(playerDownLocation) ?: return

        when (parkourPlayer.currentPoint) {
            Point.START -> {
                if (steppedPointOrder != 1) {
                    player.sendMessage("시작 포인트 안밟음")
                    return
                }
                parkourPlayer.currentPoint = Point.MIDDLE
            }
            Point.MIDDLE -> {
                // [O] currentMiddlePointOrder(1) == steppedPointOrder(2) - 1
                // [X] currentMiddlePointOrder(1) =/= steppedPointOrder(3) - 1
                if (parkourPlayer.currentMiddlePointOrder != steppedPointOrder - 1) {
                    player.sendMessage("이전 포인트 안밟음")
                    return
                }
                parkourPlayer.currentMiddlePointOrder = steppedPointOrder
            }
            else -> {}
        }
    }

    private fun stepEndPoint(player: Player) {
        val parkour = parkourRegistry.findParkourByLocation(Point.END, 1, player.downLocation) ?: return

        val playerUniqueId = player.uniqueId
        val parkourPlayer = parkour.findParkourPlayer(playerUniqueId) ?: return
        if (parkourPlayer.currentPoint != Point.MIDDLE) return

        val middleParkourPoint = parkour.getParkourPoint(Point.MIDDLE)
        if (parkourPlayer.currentMiddlePointOrder != middleParkourPoint.getLastPointOrder()) {
            player.sendMessage("MIDDLE 마지막 포인트 안밟음")
            return
        }

        player.sendMessage("§f다 깼음!! §7(걸린 시간: ${System.currentTimeMillis() - parkourPlayer.startTime})")
        parkour.removeParkourPlayer(playerUniqueId)
        parkour.findParkourPoint(Point.WAIT)?.getPointLocation()?.teleport(player)
    }

    private val Player.downLocation get() = location.block.getRelative(BlockFace.DOWN).location
}