package com.gbsb.tripmate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    USER_NOT_FOUNT("해당 유저가 없습니다."),
    MEETING_NOT_FOUNT("해당 모임이 없습니다."),
    INVALID_MEETING_TRAVEL_START_DATE("여행 시작 날짜를 확인해주세요."),
    INVALID_MEETING_TRAVEL_DATE("참여하려는 모임의 여행 기간을 확인해주세요."),
    INVALID_TRAVEL_DATE("여행 기간이 알맞지 않습니다."),
    INVALID_INPUT("입력한 내용을 확인해주세요."),
    ALREADY_JOINED_DATE("이미 참여한 여행 날짜입니다.")
    ;

    private final String description;
}
