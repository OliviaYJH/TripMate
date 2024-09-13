package com.gbsb.tripmate.dto.constant;

public enum TravelStyle {
    SIGHTSEEING("관광"),
    VACATION("휴양"),
    EXPERIENCE("체험"),
    SHOPPING("쇼핑");

    private String style;

    TravelStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
