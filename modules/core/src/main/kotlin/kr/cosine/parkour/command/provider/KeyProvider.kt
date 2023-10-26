package kr.cosine.parkour.command.provider

import kr.cosine.parkour.HQParkourModule.Companion.prefix
import kr.cosine.parkour.command.argument.KeyArgument
import kr.cosine.parkour.registry.ParkourRegistry
import kr.hqservice.framework.command.CommandArgumentProvider
import kr.hqservice.framework.command.CommandContext
import kr.hqservice.framework.command.argument.exception.ArgumentFeedback
import kr.hqservice.framework.global.core.component.Component
import org.bukkit.Location

@Component
class KeyProvider(
    private val parkourRegistry: ParkourRegistry
) : CommandArgumentProvider<KeyArgument> {

    override suspend fun cast(context: CommandContext, argument: String?): KeyArgument {
        if (argument == null) throw ArgumentFeedback.Message("$prefix 이름을 입력해주세요.")
        return KeyArgument(argument)
    }

    override suspend fun getTabComplete(context: CommandContext, location: Location?): List<String> {
        return parkourRegistry.getParkourKeys()
    }
}