package kr.cosine.parkour.enums

import kr.cosine.parkour.config.SettingConfig.Companion.prefix

enum class Reason(val message: String) {
    SUCCESSFUL(""),
    FAIL(""),
    REQUIRED_ITEM_IN_HAND("$prefix 손에 아이템을 들어주세요."),
    IS_EXIST_PARKOUR("$prefix 이미 존재하는 파쿠르입니다."),
    IS_NOT_EXIST_PARKOUR("$prefix 존재하지 않는 파쿠르입니다."),
    FAILED_TO_PARSE("$prefix PointLocation으로 변환하는데 실패하였습니다."),
    IS_NOT_MIDDLE_POINT_LOCATION("$prefix 등록되지 않은 중간 지점입니다.")
}