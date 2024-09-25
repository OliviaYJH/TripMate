package com.gbsb.tripmate.exception;

import com.gbsb.tripmate.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.UnsupportedEncodingException;

import static com.gbsb.tripmate.enums.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MeetingException.class)
    public ErrorResponse handleMeetingException(MeetingException e) {
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ErrorResponse(INVALID_REQUEST, INVALID_REQUEST.getDescription());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getDescription());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ErrorResponse(INVALID_INPUT, INVALID_INPUT.getDescription());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ErrorResponse(INVALID_INPUT, INVALID_INPUT.getDescription());
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ErrorResponse handleUnsupportedEncodingException(UnsupportedEncodingException e) {
        return new ErrorResponse(FAIL_ENCODING, FAIL_ENCODING.getDescription());
    }
}
