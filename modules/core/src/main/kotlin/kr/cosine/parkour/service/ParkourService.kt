package kr.cosine.parkour.service

import kr.cosine.parkour.enums.Announce
import kr.cosine.parkour.enums.Error
import kr.cosine.parkour.enums.Point
import kr.cosine.parkour.registry.AnnounceRegistry
import kr.cosine.parkour.registry.ErrorMessageRegistry
import kr.cosine.parkour.registry.ParkourRegistry
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.Location
import org.bukkit.entity.Player

@Service
class ParkourService(
    private val errorMessageRegistry: ErrorMessageRegistry,
    private val announceRegistry: AnnounceRegistry,
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
        if (parkour.isParkorPlayer(playerUniqueId)) {
            errorMessageRegistry.getErrorMessage(Error.PARKOUR_STARTED).sendMessage(player)
            return
        }
        announceRegistry.getParkourAnnounce(Announce.START_PARKOUR).announce(player)
        parkour.setParkourPlayer(playerUniqueId).apply {
            startTime = System.currentTimeMillis()
        }
    }

    private fun stepMiddlePoint(player: Player, location: Location) {
        val parkour = parkourRegistry.findParkourByLocation(Point.MIDDLE, location) ?: return

        val playerUniqueId = player.uniqueId
        val parkourPlayer = parkour.findParkourPlayer(playerUniqueId) ?: return

        val middleParkourPoint = parkour.findParkourPoint(Point.MIDDLE) ?: return
        val steppedPointOrder = middleParkourPoint.getPointOrderByLocation(location) ?: return

        when (parkourPlayer.currentPoint) {
            Point.START -> {
                if (steppedPointOrder != 1) {
                    errorMessageRegistry.getErrorMessage(Error.NOT_STEPPED_BEFORE_POINT).sendMessage(player)
                    return
                }
                parkourPlayer.currentPoint = Point.MIDDLE
            }
            Point.MIDDLE -> {
                // [O] currentMiddlePointOrder(1) == steppedPointOrder(2) - 1
                // [X] currentMiddlePointOrder(1) =/= steppedPointOrder(3) - 1
                if (steppedPointOrder <= parkourPlayer.currentMiddlePointOrder) {
                    errorMessageRegistry.getErrorMessage(Error.STEPPED_POINT).sendMessage(player)
                    return
                }
                if (parkourPlayer.currentMiddlePointOrder != steppedPointOrder - 1) {
                    errorMessageRegistry.getErrorMessage(Error.NOT_STEPPED_BEFORE_POINT).sendMessage(player)
                    return
                }
                parkourPlayer.currentMiddlePointOrder = steppedPointOrder
                announceRegistry.getParkourAnnounce(Announce.MIDDLE_PARKOUR).apply {
                    val replaceFunction: (String) -> String = {
                        it.replace("%order%", parkourPlayer.currentMiddlePointOrder.toString())
                    }
                    sound.playSound(player)
                    chat.sendMessage(player, replaceFunction)
                    title.sendTitle(player, replaceFunction)
                }
            }
            else -> {}
        }
    }

    private fun stepEndPoint(player: Player, location: Location) {
        val parkour = parkourRegistry.findParkourByLocation(Point.END, location) ?: return

        val playerUniqueId = player.uniqueId
        val parkourPlayer = parkour.findParkourPlayer(playerUniqueId) ?: return
        if (parkourPlayer.currentPoint != Point.MIDDLE) return

        val middleParkourPoint = parkour.findParkourPoint(Point.MIDDLE) ?: return
        if (parkourPlayer.currentMiddlePointOrder != middleParkourPoint.getLastPointOrder()) {
            errorMessageRegistry.getErrorMessage(Error.NOT_STEPPED_BEFORE_POINT).sendMessage(player)
            return
        }
        announceRegistry.getParkourAnnounce(Announce.END_PARKOUR).apply {
            val replaceFunction: (String) -> String = {
                it.replace("%player%", player.name)
                    .replace("%time%", (System.currentTimeMillis() - parkourPlayer.startTime).toString())
            }
            sound.playSound(player)
            chat.sendMessage(player, replaceFunction)
            title.sendTitle(player, replaceFunction)
        }
        parkour.removeParkourPlayer(playerUniqueId)
        parkour.getReward()?.let { player.inventory.addItem(it) }
        parkour.findParkourPoint(Point.WAIT)?.getPointLocation()?.teleport(player)
    }
}