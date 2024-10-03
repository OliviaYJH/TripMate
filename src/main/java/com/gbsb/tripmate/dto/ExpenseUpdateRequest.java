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
@Schema(description = "지출 수정 요청 DTO")
public class ExpenseUpdateRequest {
    @Schema(description = "수정된 지출 설명", example = "점심 식사로 수정")
    private String expenseDescription;

    @Schema(description = "수정된 지출 금액", example = "30000.0")
    private Float expenseAmount;

    @Schema(description = "수정된 지출 날짜", example = "2023-05-16")
    private LocalDate expenseDate;

    @Schema(description = "수정된 그룹 지출 여부", example = "false")
    private Boolean isGroupExpense;
}
