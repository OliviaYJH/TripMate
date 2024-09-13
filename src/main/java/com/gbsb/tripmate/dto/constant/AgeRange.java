package com.gbsb.tripmate.dto.constant;

public enum AgeRange {
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대 이상");

    private String description;

    AgeRange(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
