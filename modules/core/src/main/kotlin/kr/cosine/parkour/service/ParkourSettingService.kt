package kr.cosine.parkour.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.cosine.parkour.config.ParkourConfig
import kr.cosine.parkour.config.SettingConfig
import kr.cosine.parkour.data.Parkour
import kr.cosine.parkour.data.toPointLocation
import kr.cosine.parkour.enums.Point
import kr.cosine.parkour.enums.Reason
import kr.cosine.parkour.extension.upBlockLocation
import kr.cosine.parkour.registry.ParkourRegistry
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@Service
class ParkourSettingService(
    private val settingConfig: SettingConfig,
    private val parkourRegistry: ParkourRegistry,
    private val parkourConfig: ParkourConfig
) {

    fun createParkour(key: String): Reason {
        if (parkourRegistry.isParkour(key)) return Reason.IS_EXIST_PARKOUR
        val parkour = Parkour(key)
        parkourRegistry.setParkour(key, parkour)
        return Reason.SUCCESSFUL
    }

    suspend fun deleteParkour(key: String): Reason {
        if (!parkourRegistry.isParkour(key)) return Reason.IS_NOT_EXIST_PARKOUR
        return if (parkourConfig.delete(key)) {
            Reason.SUCCESSFUL
        } else {
            Reason.FAIL
        }
    }

    fun setParkourReward(key: String, itemStack: ItemStack): Reason {
        val parkour = parkourRegistry.findParkour(key) ?: return Reason.IS_NOT_EXIST_PARKOUR
        if (itemStack.type.isAir) return Reason.REQUIRED_ITEM_IN_HAND
        parkour.setReward(itemStack)
        return Reason.SUCCESSFUL
    }

    fun setParkourWaitPoint(key: String, location: Location): Reason {
        return setParkourPoint(key, Point.WAIT, 1, location, false)
    }

    fun setParkourStartPoint(key: String, location: Location): Reason {
        return setParkourPoint(key, Point.START, 1, location)
    }

    fun setParkourEndPoint(key: String, location: Location): Reason {
        return setParkourPoint(key, Point.END, 1, location)
    }

    fun setParkourMiddlePoint(key: String, order: Int, location: Location): Reason {
        return setParkourPoint(key, Point.MIDDLE, order, location)
    }

    private fun setParkourPoint(key: String, point: Point, order: Int, location: Location, setBlock: Boolean = true): Reason {
        val parkour = parkourRegistry.findParkour(key) ?: return Reason.IS_NOT_EXIST_PARKOUR
        val pointLocation = location.toPointLocation() ?: return Reason.FAILED_TO_PARSE
        parkour.setParkourPointLocation(point, order, pointLocation)
        if (setBlock) {
            location.upBlockLocation.block.type = Material.STONE_PRESSURE_PLATE
        }
        return Reason.SUCCESSFUL
    }

    fun removeParkourMiddlePoint(key: String, order: Int): Reason {
        val parkour = parkourRegistry.findParkour(key) ?: return Reason.IS_NOT_EXIST_PARKOUR
        val middleParkourPoint = parkour.getParkourPoint(Point.MIDDLE)
        if (!middleParkourPoint.isPointLocation(order)) return Reason.IS_NOT_MIDDLE_POINT_LOCATION
        middleParkourPoint.removePointLocation(order)
        return Reason.SUCCESSFUL
    }

    fun findParkour(key: String): Parkour? {
        return parkourRegistry.findParkour(key)
    }

    suspend fun save() {
        withContext(Dispatchers.IO) {
            parkourConfig.save()
        }
    }

    fun reload() {
        settingConfig.reload()
    }
}