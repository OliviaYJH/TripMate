package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.ExpenseCreateRequest;
import com.gbsb.tripmate.dto.ExpenseResponse;
import com.gbsb.tripmate.dto.ExpenseUpdateRequest;
import com.gbsb.tripmate.entity.Expense;
import com.gbsb.tripmate.service.CustomUserDetailsService;
import com.gbsb.tripmate.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Expenses", description = "가계부 API")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "지출 생성", description = "새로운 지출 항목을 생성합니다")
    public BaseResponse<ExpenseResponse> createExpense(
            @Parameter(description = "인증된 사용자") @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user,
            @Parameter(description = "지출 생성 요청") @RequestBody ExpenseCreateRequest request) {
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
    @Operation(summary = "지출 수정", description = "기존 지출 항목을 수정합니다")
    public BaseResponse<ExpenseResponse> updateExpense(
            @Parameter(description = "인증된 사용자") @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user,
            @Parameter(description = "지출 ID") @PathVariable Long expenseId,
            @Parameter(description = "지출 수정 요청") @RequestBody ExpenseUpdateRequest request) {
        Expense expense = expenseService.updateExpense(user.getId(), expenseId, request);
        return new BaseResponse<>("가계부 항목이 수정되었습니다.", convertToExpenseResponse(expense));
    }

    @PutMapping("/{expenseId}/delete")
    @Operation(summary = "지출 삭제 상태 변경", description = "지출 항목의 삭제 상태를 변경합니다")
    public BaseResponse<Void> toggleDeleteExpense(
            @Parameter(description = "인증된 사용자") @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user,
            @Parameter(description = "지출 ID") @PathVariable Long expenseId) {
        expenseService.toggleDeleteExpense(user.getId(), expenseId);
        return new BaseResponse<>("가계부 항목 삭제 상태가 변경되었습니다.", null);
    }

    @GetMapping("/meeting/{meetingId}")
    @Operation(summary = "모임별 지출 조회", description = "특정 모임의 모든 지출을 조회합니다")
    public BaseResponse<List<ExpenseResponse>> getExpensesByMeeting(
            @Parameter(description = "모임 ID") @PathVariable Long meetingId) {
        List<Expense> expenses = expenseService.getExpensesByMeeting(meetingId);
        List<ExpenseResponse> expenseResponses = expenses.stream()
                .map(this::convertToExpenseResponse)
                .collect(Collectors.toList());
        return new BaseResponse<>("가계부 항목 조회 성공", expenseResponses);
    }

    @GetMapping("/meeting/{meetingId}/per-person")
    @Operation(summary = "인당 지출 계산", description = "모임의 인당 지출을 계산합니다")
    public BaseResponse<Float> getPerPersonExpense(
            @Parameter(description = "모임 ID") @PathVariable Long meetingId) {
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
