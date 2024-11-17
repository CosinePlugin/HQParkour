package kr.cosine.parkour.service

import kr.cosine.parkour.enums.Announce
import kr.cosine.parkour.enums.MessageType
import kr.cosine.parkour.enums.Point
import kr.cosine.parkour.event.ParkourSuccessEvent
import kr.cosine.parkour.extension.toKoreanTimeFormat
import kr.cosine.parkour.registry.*
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.giftbox.api.GiftBoxAPI
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager

@Service
class ParkourService(
    private val pluginManager: PluginManager,
    private val settingRegistry: SettingRegistry,
    private val messageRegistry: MessageRegistry,
    private val announceRegistry: AnnounceRegistry,
    private val parkourRegistry: ParkourRegistry,
    private val parkourRankingHolderRegistry: ParkourRankingHolderRegistry
) {

    private val giftBoxFactory by lazy { GiftBoxAPI.getFacto() }
    private val giftBoxService by lazy { GiftBoxAPI.getBoxService() }

    fun isCommandUsePrevented(player: Player): Boolean {
        val playerUniqueId = player.uniqueId
        val parkour = parkourRegistry.findParkourByPlayer(playerUniqueId) ?: return false
        if (!player.isOp && settingRegistry.preventedCommand && parkour.isParkorPlayer(playerUniqueId)) {
            messageRegistry.getMessage(MessageType.PREVENTED_COMMAND).sendMessage(player)
            return true
        }
        return false
    }

    fun stepChecker(player: Player, location: Location) {
        stepStartPoint(player, location)
        stepMiddlePoint(player, location)
        stepEndPoint(player, location)
    }

    fun removeFromParkourPlayer(player: Player) {
        val playerUniqueId = player.uniqueId
        val parkour = parkourRegistry.findParkourByPlayer(playerUniqueId) ?: return
        parkour.removeParkourPlayer(playerUniqueId)
        parkour.getParkourPoint(Point.WAIT).getPointLocation().teleport(player, plusY = 0.0)
    }

    fun parkourKeyAction(player: Player, slot: Int): Boolean {
        val playerUniqueId = player.uniqueId
        val parkour = parkourRegistry.findParkourByPlayer(playerUniqueId) ?: return false
        val parkourPlayer = parkour.getParkourPlayer(playerUniqueId)
        when (slot) {
            0 -> {
                when (val point = parkourPlayer.currentPoint) {
                    Point.START -> {
                        val parkourPoint = parkour.getParkourPoint(point)
                        parkourPoint.getPointLocation().teleport(player)
                    }

                    Point.MIDDLE -> {
                        val parkourPoint = parkour.getParkourPoint(point)
                        val currentMiddlePointOrder = parkourPlayer.currentMiddlePointOrder
                        parkourPoint.getPointLocation(currentMiddlePointOrder).teleport(player)
                    }

                    else -> {}
                }
            }

            8 -> {
                val waitParkourPoint = parkour.getParkourPoint(Point.WAIT)
                waitParkourPoint.getPointLocation().teleport(player, plusY = 0.0)
                parkour.removeParkourPlayer(playerUniqueId)
                messageRegistry.getMessage(MessageType.GIVE_UP_PARKOUR).sendMessage(player)
            }

            else -> {}
        }
        return true
    }

    private fun stepStartPoint(player: Player, location: Location) {
        val parkour = parkourRegistry.findParkourByLocation(Point.START, location) ?: return

        val playerUniqueId = player.uniqueId
        if (parkour.isParkorPlayer(playerUniqueId)) {
            messageRegistry.getMessage(MessageType.PARKOUR_STARTED).sendMessage(player)
            return
        }
        player.inventory.heldItemSlot = 4
        announceRegistry.getParkourAnnounce(Announce.START_PARKOUR).announce(player)
        messageRegistry.getMessage(MessageType.PARKOUR_KEY_INFO).sendMessage(player)
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
                    messageRegistry.getMessage(MessageType.NOT_STEPPED_BEFORE_POINT).sendMessage(player)
                    return
                }
                parkourPlayer.currentPoint = Point.MIDDLE
                player.sendMiddleParkourMessage(parkourPlayer.currentMiddlePointOrder)
            }

            Point.MIDDLE -> {
                // [O] currentMiddlePointOrder(1) == steppedPointOrder(2) - 1
                // [X] currentMiddlePointOrder(1) =/= steppedPointOrder(3) - 1
                if (steppedPointOrder <= parkourPlayer.currentMiddlePointOrder) {
                    messageRegistry.getMessage(MessageType.STEPPED_POINT).sendMessage(player)
                    return
                }
                if (parkourPlayer.currentMiddlePointOrder != steppedPointOrder - 1) {
                    messageRegistry.getMessage(MessageType.NOT_STEPPED_BEFORE_POINT).sendMessage(player)
                    return
                }
                parkourPlayer.currentMiddlePointOrder = steppedPointOrder
                player.sendMiddleParkourMessage(parkourPlayer.currentMiddlePointOrder)
            }

            else -> {}
        }
    }

    private fun Player.sendMiddleParkourMessage(currentMiddlePointOrder: Int) {
        announceRegistry.getParkourAnnounce(Announce.MIDDLE_PARKOUR).apply {
            val replaceFunction: (String) -> String = {
                it.replace("%order%", currentMiddlePointOrder.toString())
            }
            sound.playSound(this@sendMiddleParkourMessage)
            chat.sendMessage(this@sendMiddleParkourMessage, replaceFunction)
            title.sendTitle(this@sendMiddleParkourMessage, replaceFunction)
        }
    }

    private fun stepEndPoint(player: Player, location: Location) {
        val parkour = parkourRegistry.findParkourByLocation(Point.END, location) ?: return
        val key = parkour.key

        val playerUniqueId = player.uniqueId
        val parkourPlayer = parkour.findParkourPlayer(playerUniqueId) ?: return
        if (parkourPlayer.currentPoint != Point.MIDDLE) return

        val middleParkourPoint = parkour.findParkourPoint(Point.MIDDLE) ?: return
        if (parkourPlayer.currentMiddlePointOrder != middleParkourPoint.getLastPointOrder()) {
            messageRegistry.getMessage(MessageType.NOT_STEPPED_BEFORE_POINT).sendMessage(player)
            return
        }
        parkour.removeParkourPlayer(playerUniqueId)
        parkour.findParkourPoint(Point.WAIT)?.getPointLocation()?.teleport(player, plusY = 0.0)
        val time = System.currentTimeMillis() - parkourPlayer.startTime
        announceRegistry.getParkourAnnounce(Announce.END_PARKOUR).apply {
            val replaceFunction: (String) -> String = {
                it.replace("%player%", player.name)
                    .replace("%time%", time.toKoreanTimeFormat())
            }
            sound.playSound(player)
            chat.sendMessage(player, replaceFunction)
            title.sendTitle(player, replaceFunction)
        }
        val rankingHolder = parkourRankingHolderRegistry.getRankingHolder(key)
        if (rankingHolder.refresh(player, time)) {
            parkourRankingHolderRegistry.isChanged = true
        }

        val reward = parkour.getReward()
        val parkourSuccessEvent = ParkourSuccessEvent(player, key, reward, time)
        pluginManager.callEvent(parkourSuccessEvent)
        if (parkourSuccessEvent.isCancelled || reward == null) return

        if (settingRegistry.useGiftBox) {
            val giftBox = giftBoxFactory.of("§e파쿠르 보상", listOf("§f파쿠르 클리어를 축하드립니다!"), reward)
            giftBoxService.send(playerUniqueId, giftBox)
            messageRegistry.getMessage(MessageType.REWARD_TO_GIFTBOX).sendMessage(player)
        } else {
            player.inventory.addItem(reward)
            messageRegistry.getMessage(MessageType.REWARD_TO_INVENTORY).sendMessage(player)
        }
    }
}