package com.gbsb.tripmate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {
    private Long expenseId;
    private Long meetingId;
    private String createdBy;
    private String expenseDescription;
    private Float expenseAmount;
    private LocalDate expenseDate;
    private Boolean isGroupExpense;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
