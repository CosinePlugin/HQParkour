package kr.cosine.parkour.enums

enum class MessageType {
    PARKOUR_STARTED,
    NOT_STEPPED_BEFORE_POINT,
    STEPPED_POINT,
    REWARD_TO_INVENTORY,
    REWARD_TO_GIFTBOX,
    PARKOUR_KEY_INFO,
    GIVE_UP_PARKOUR;

    companion object {
        fun getError(messageText: String): MessageType? {
            return values().find { it.name == messageText.uppercase().replace("-", "_") }
        }
    }
}