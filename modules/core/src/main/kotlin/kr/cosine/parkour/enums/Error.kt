package kr.cosine.parkour.enums

enum class Error {
    PARKOUR_STARTED,
    NOT_STEPPED_BEFORE_POINT,
    STEPPED_POINT;

    companion object {
        fun getError(messageText: String): Error? {
            return values().find { it.name == messageText.uppercase().replace("-", "_") }
        }
    }
}