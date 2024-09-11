package com.gbsb.tripmate.dto;

import lombok.Getter;

@Getter
public class BaseResponse <T> {

    private final String message;
    private final T data;

    public BaseResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}