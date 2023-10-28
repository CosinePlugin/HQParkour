package kr.cosine.parkour.registry

import kr.cosine.parkour.data.Message
import kr.cosine.parkour.enums.MessageType
import kr.hqservice.framework.global.core.component.Bean

@Bean
class MessageRegistry {

    private val messageMap = mutableMapOf<MessageType, Message>()

    fun findMessage(messageType: MessageType): Message? = messageMap[messageType]

    fun getMessage(messageType: MessageType): Message = findMessage(messageType) ?: throw IllegalArgumentException()

    fun setMessage(messageType: MessageType, message: Message) {
        messageMap[messageType] = message
    }

    internal fun clear() {
        messageMap.clear()
    }
}