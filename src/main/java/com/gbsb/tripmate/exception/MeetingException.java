package com.gbsb.tripmate.exception;

import com.gbsb.tripmate.enums.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public MeetingException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
