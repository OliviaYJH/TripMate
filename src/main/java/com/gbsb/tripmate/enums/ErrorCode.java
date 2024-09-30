package com.gbsb.tripmate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    USER_NOT_FOUND("해당 유저가 없습니다."),
    MEETING_NOT_FOUND("해당 모임이 없습니다."),
    INVALID_MEETING_TRAVEL_START_DATE("여행 시작 날짜를 확인해주세요."),
    INVALID_MEETING_TRAVEL_DATE("참여하려는 모임의 여행 기간을 확인해주세요."),
    INVALID_TRAVEL_DATE("여행 기간이 알맞지 않습니다."),
    INVALID_INPUT("입력한 내용을 확인해주세요."),
    ALREADY_JOINED_DATE("이미 참여한 여행 날짜입니다."),
    USER_AND_MEETING_UNMATCHED("해당 유저가 생성한 모임이 아닙니다."),
    CREATED_BY_USER("해당 유저가 생성한 모임입니다."),
    FAIL_ENCODING("정보를 가져오지 못했습니다."),
    INVALID_ADDRESS("주소에 해당하는 장소가 없습니다."),
    ALREADY_DATE_EXIST("해당 날짜에 해당하는 계획이 이미 존재합니다."),
    PLAN_NOT_FOUND("해당하는 여행 계획이 없습니다."),
    PLACE_NOT_FOUND("해당 장소는 없는 장소입니다."),
    INVALID_ITEM_ORDER("해당 순서는 존재하지 않습니다."),
    ALREADY_SAME_START_TIME_EXIST("해당 시간에 시작하는 여행 계획이 존재합니다."),
    INSUFFICIENT_PLAN_ITEMS("최소 2개 이상의 계획 항목이 필요합니다."),
    NO_ROUTE_FOUND("경로를 찾을 수 없습니다."),
    JSON_PARSING_ERROR("JSON 파싱 중 오류가 발생했습니다."),
    KAKAO_API_ERROR("카카오 API 사용 중 오류가 발생했습니다."),
    PLAN_ITEM_ID_NOT_FOUND("해당 세부 일정이 없습니다.");

    private final String description;
}
