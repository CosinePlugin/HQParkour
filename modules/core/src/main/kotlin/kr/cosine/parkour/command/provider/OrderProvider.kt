package kr.cosine.parkour.command.provider

import kr.cosine.parkour.registry.SettingRegistry.Companion.prefix
import kr.cosine.parkour.command.argument.OrderArgument
import kr.hqservice.framework.command.CommandArgumentProvider
import kr.hqservice.framework.command.CommandContext
import kr.hqservice.framework.command.argument.exception.ArgumentFeedback
import kr.hqservice.framework.global.core.component.Component
import org.bukkit.Location

@Component
class OrderProvider : CommandArgumentProvider<OrderArgument> {

    private val commandTabList = (1..10).map(Int::toString)

    override suspend fun cast(context: CommandContext, argument: String?): OrderArgument {
        if (argument == null) throw ArgumentFeedback.Message("$prefix 순서를 입력해주세요.")
        val order = argument.toIntOrNull() ?: throw ArgumentFeedback.Message("$prefix 숫자만 입력할 수 있습니다.")
        if (order < 1) throw ArgumentFeedback.Message("$prefix 양의 정수만 입력할 수 있습니다.")
        return OrderArgument(order)
    }

    override suspend fun getTabComplete(context: CommandContext, location: Location?): List<String> {
        return commandTabList
    }
}