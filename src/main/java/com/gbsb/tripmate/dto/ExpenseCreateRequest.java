package com.gbsb.tripmate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCreateRequest {
    private Long meetingId;
    private String expenseDescription;
    private Float expenseAmount;
    private LocalDate expenseDate;
    private Boolean isGroupExpense;
}
