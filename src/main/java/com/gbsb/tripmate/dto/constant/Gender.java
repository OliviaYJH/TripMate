package com.gbsb.tripmate.dto.constant;

public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    ALL("전체");

    private String code;

    Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
