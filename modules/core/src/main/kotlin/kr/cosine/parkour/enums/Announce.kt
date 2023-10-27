package kr.cosine.parkour.enums

enum class Announce {
    START_PARKOUR,
    MIDDLE_PARKOUR,
    END_PARKOUR;

    companion object {
        fun getAnnounce(announceText: String): Announce? {
            return values().find { it.name == announceText.uppercase().replace("-", "_") }
        }
    }
}