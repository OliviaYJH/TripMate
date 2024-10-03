package com.gbsb.tripmate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "지출 생성 요청 DTO")
public class ExpenseCreateRequest {
    @Schema(description = "모임 ID", example = "1")
    private Long meetingId;

    @Schema(description = "지출 설명", example = "저녁 식사")
    private String expenseDescription;

    @Schema(description = "지출 금액", example = "50000.0")
    private Float expenseAmount;

    @Schema(description = "지출 날짜", example = "2023-05-15")
    private LocalDate expenseDate;

    @Schema(description = "그룹 지출 여부", example = "true")
    private Boolean isGroupExpense;
}
