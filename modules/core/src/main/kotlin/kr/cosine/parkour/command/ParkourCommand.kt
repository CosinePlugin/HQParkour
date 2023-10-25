package kr.cosine.parkour.command

import kr.cosine.parkour.command.argument.KeyArgument
import kr.cosine.parkour.command.argument.OrderArgument
import kr.hqservice.framework.command.ArgumentLabel
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.entity.Player

@Command(label = "파쿠르관리", isOp = true)
class ParkourCommand {

    @CommandExecutor("생성", "파쿠르를 생성합니다.", priority = 1)
    fun createParkour(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {

    }

    @CommandExecutor("제거", "파쿠르를 제거합니다.", priority = 2)
    fun deleteParkour(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {

    }

    @CommandExecutor("시작지점", "현재 위치를 파쿠르의 시작 지점으로 설정합니다.", priority = 3)
    fun setParkourStartPoint(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {

    }

    @CommandExecutor("종료지점", "현재 위치를 파쿠르의 종료 지점으로 설정합니다.", priority = 4)
    fun setParkourEndPoint(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument
    ) {

    }

    @CommandExecutor("중간지점", "현재 위치를 파쿠르의 n번 중간 지점으로 설정합니다.", priority = 5)
    fun setParkourMiddlePoint(
        player: Player,
        @ArgumentLabel("이름") keyArgument: KeyArgument,
        @ArgumentLabel("순서") orderArgument: OrderArgument
    ) {

    }
}