package kr.cosine.parkour.command

import kr.cosine.parkour.command.argument.KeyArgument
import kr.cosine.parkour.command.argument.OrderArgument
import kr.cosine.parkour.registry.SettingRegistry.Companion.prefix
import kr.cosine.parkour.enums.Point
import kr.cosine.parkour.enums.Reason
import kr.cosine.parkour.extension.downLocation
import kr.cosine.parkour.service.ParkourSettingService
import kr.hqservice.framework.command.ArgumentLabel
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.entity.Player

@Command(label = "파쿠르관리", isOp = true)
class ParkourAdminCommand(
    private val parkourSettingService: ParkourSettingService
) {

    @CommandExecutor("생성", "파쿠르를 생성합니다.", priority = 1)
    fun createParkour(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        val reason = parkourSettingService.createParkour(key)
        execute(player, reason) {
            player.sendMessage("$prefix $key 파쿠르를 생성하였습니다.")
        }
    }

    @CommandExecutor("제거", "파쿠르를 제거합니다.", priority = 2)
    suspend fun deleteParkour(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        val reason = parkourSettingService.deleteParkour(key)
        execute(player, reason) {
            player.sendMessage("$prefix $key 파쿠르를 제거하였습니다.")
        }
    }

    @CommandExecutor("보상설정", "손에 들고 있는 아이템을 파쿠르 보상으로 설정합니다.", priority = 3)
    fun setParkourReward(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        val reason = parkourSettingService.setParkourReward(key, player.inventory.itemInMainHand.clone())
        execute(player, reason) {
            player.sendMessage("$prefix $key 파쿠르의 보상을 설정하였습니다.")
        }
    }

    @CommandExecutor("대기지점", "현지 위치를 파쿠르의 대기 지점으로 설정합니다.", priority = 4)
    fun setParkourWaitPoint(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        val reason = parkourSettingService.setParkourWaitPoint(key, player.location)
        execute(player, reason) {
            player.sendMessage("$prefix 현재 위치를 $key 파쿠르의 대기 지점으로 설정하였습니다.")
        }
    }

    @CommandExecutor("시작지점", "현재 위치를 파쿠르의 시작 지점으로 설정합니다.", priority = 5)
    fun setParkourStartPoint(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        val reason = parkourSettingService.setParkourStartPoint(key, player.downLocation)
        execute(player, reason){
            player.sendMessage("$prefix 현재 위치를 $key 파쿠르의 시작 지점으로 설정하였습니다.")
        }
    }

    @CommandExecutor("중간지점", "현재 위치를 파쿠르의 n번 중간 지점으로 설정합니다.", priority = 6)
    fun setParkourMiddlePoint(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument,
        @ArgumentLabel("순서") orderArgument: OrderArgument
    ) {
        val key = keyArgument.key
        val order = orderArgument.order
        val reason = parkourSettingService.setParkourMiddlePoint(key, order, player.downLocation)
        execute(player, reason) {
            player.sendMessage("$prefix 현재 위치를 $key 파쿠르의 ${order}번째 중간 지점으로 설정하였습니다.")
        }
    }

    @CommandExecutor("중간지점제거", "파쿠르의 n번 중간 지점을 제거합니다.", priority = 7)
    fun removeParkourMiddlePoint(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument,
        @ArgumentLabel("순서") orderArgument: OrderArgument
    ) {
        val key = keyArgument.key
        val order = orderArgument.order
        val reason = parkourSettingService.removeParkourMiddlePoint(key, order)
        execute(player, reason) {
            player.sendMessage("$prefix $key 파쿠르의 ${order}번째 중간 지점을 제거하였습니다.")
        }
    }

    @CommandExecutor("종료지점", "현재 위치를 파쿠르의 종료 지점으로 설정합니다.", priority = 8)
    fun setParkourEndPoint(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        val reason = parkourSettingService.setParkourEndPoint(key, player.downLocation)
        execute(player, reason) {
            player.sendMessage("$prefix 현재 위치를 $key 파쿠르의 종료 지점으로 설정하였습니다.")
        }
    }

    @CommandExecutor("정보", "파쿠르의 정보를 확인합니다.", priority = 9)
    fun showParkourInfo(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        val parkour = parkourSettingService.findParkour(key) ?: run {
            player.sendMessage(Reason.IS_NOT_EXIST_PARKOUR.message)
            return
        }
        player.sendMessage("$prefix $key 정보")
        parkour.findParkourPoint(Point.WAIT)?.let {
            player.sendMessage("§3[대기] §f${it.getPointLocation().format()}")
        }
        parkour.findParkourPoint(Point.START)?.let {
            player.sendMessage("§a[시작] §f${it.getPointLocation().format()}")
        }
        parkour.findParkourPoint(Point.MIDDLE)?.let {
            it.getPointLocationMap().forEach { (order, pointLocation) ->
                player.sendMessage("§6[중간-$order] §f${pointLocation.format()}")
            }
        }
        parkour.findParkourPoint(Point.END)?.let {
            player.sendMessage("§c[종료] §f${it.getPointLocation().format()}")
        }
    }

    @CommandExecutor("저장", "모든 파쿠르의 변경 사항을 저장합니다.", priority = 10)
    suspend fun save(player: Player) {
        parkourSettingService.save()
        player.sendMessage("$prefix 모든 파쿠르의 변경 사항을 저장하였습니다.")
    }

    private fun execute(player: Player, reason: Reason, successfulFunction: () -> Unit) {
        when (reason) {
            Reason.SUCCESSFUL -> successfulFunction()
            else -> player.sendMessage(reason.message)
        }
    }

    @CommandExecutor("리로드", "config.yml을 리로드합니다.", priority = 11)
    fun reload(player: Player) {
        parkourSettingService.reload()
        player.sendMessage("$prefix config.yml을 리로드하였습니다.")
    }
}