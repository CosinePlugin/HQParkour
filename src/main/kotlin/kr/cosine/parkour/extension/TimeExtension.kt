package kr.cosine.parkour.extension

internal fun Long.toKoreanTimeFormat(): String {
    if (this == 0L) return "0초"
    val days = this / (1000 * 60 * 60 * 24)
    val hours = (this % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
    val minutes = (this % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (this % (1000 * 60)) / 1000
    return buildString {
        if (days != 0L) append("${days}일 ")
        if (hours != 0L) append("${hours}시간 ")
        if (minutes != 0L) append("${minutes}분 ")
        if (seconds != 0L) append(("${seconds}초"))
    }.removeSuffix(" ")
}