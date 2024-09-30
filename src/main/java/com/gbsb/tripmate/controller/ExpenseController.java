package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.ExpenseCreateRequest;
import com.gbsb.tripmate.dto.ExpenseResponse;
import com.gbsb.tripmate.dto.ExpenseUpdateRequest;
import com.gbsb.tripmate.entity.Expense;
import com.gbsb.tripmate.service.CustomUserDetailsService;
import com.gbsb.tripmate.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public BaseResponse<ExpenseResponse> createExpense(@AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user,
                                                       @RequestBody ExpenseCreateRequest request) {
        log.debug("Creating expense: {}", request);
        try {
            Expense expense = expenseService.createExpense(user.getId(), request);
            log.debug("Expense created successfully: {}", expense);
            return new BaseResponse<>("가계부 항목이 추가되었습니다.", convertToExpenseResponse(expense));
        } catch (Exception e) {
            log.error("Error creating expense", e);
            return new BaseResponse<>("가계부 항목 추가 중 오류가 발생했습니다.", null);
        }
    }

    @PutMapping("/{expenseId}")
    public BaseResponse<ExpenseResponse> updateExpense(@AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user,
                                                       @PathVariable Long expenseId,
                                                       @RequestBody ExpenseUpdateRequest request) {
        Expense expense = expenseService.updateExpense(user.getId(), expenseId, request);
        return new BaseResponse<>("가계부 항목이 수정되었습니다.", convertToExpenseResponse(expense));
    }

    @PutMapping("/{expenseId}/delete")
    public BaseResponse<Void> toggleDeleteExpense(@AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user,
                                                  @PathVariable Long expenseId) {
        expenseService.toggleDeleteExpense(user.getId(), expenseId);
        return new BaseResponse<>("가계부 항목 삭제 상태가 변경되었습니다.", null);
    }

    @GetMapping("/meeting/{meetingId}")
    public BaseResponse<List<ExpenseResponse>> getExpensesByMeeting(@PathVariable Long meetingId) {
        List<Expense> expenses = expenseService.getExpensesByMeeting(meetingId);
        List<ExpenseResponse> expenseResponses = expenses.stream()
                .map(this::convertToExpenseResponse)
                .collect(Collectors.toList());
        return new BaseResponse<>("가계부 항목 조회 성공", expenseResponses);
    }

    @GetMapping("/meeting/{meetingId}/per-person")
    public BaseResponse<Float> getPerPersonExpense(@PathVariable Long meetingId) {
        Float perPersonExpense = expenseService.calculatePerPersonExpense(meetingId);
        return new BaseResponse<>("인당 경비 계산 완료", perPersonExpense);
    }

    private ExpenseResponse convertToExpenseResponse(Expense expense) {
        return ExpenseResponse.builder()
                .expenseId(expense.getExpenseId())
                .meetingId(expense.getMeeting().getMeetingId())
                .createdBy(expense.getCreatedBy().getNickname())
                .expenseDescription(expense.getExpenseDescription())
                .expenseAmount(expense.getExpenseAmount())
                .expenseDate(expense.getExpenseDate())
                .isGroupExpense(expense.getIsGroupExpense())
                .createdDate(expense.getCreatedDate())
                .modifiedDate(expense.getModifiedDate())
                .build();
    }
}
