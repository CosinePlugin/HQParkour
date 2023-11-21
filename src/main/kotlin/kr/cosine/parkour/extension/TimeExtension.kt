package kr.cosine.parkour.extension

internal fun Long.toKoreanTimeFormat(): String {
    val hours = this / (1000 * 60 * 60)
    val minutes = this / (1000 * 60)
    val seconds = (this / 1000) % 60
    return buildString {
        if (hours != 0L) append("${hours}시간 ")
        if (minutes != 0L) append("${minutes}분 ")
        if (seconds != 0L) append(("${seconds}초"))
    }
}