package kr.cosine.parkour.enums

enum class Point {
    WAIT,
    START,
    MIDDLE,
    END;

    companion object {
        fun getPoint(pointText: String): Point? {
            return values().find { it.name == pointText.uppercase() }
        }
    }
}