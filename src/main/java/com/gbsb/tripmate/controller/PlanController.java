package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.PlanCreate;
import com.gbsb.tripmate.dto.PlanItemCreate;
import com.gbsb.tripmate.dto.PlanItemResponse;
import com.gbsb.tripmate.service.TravelPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
@Tag(name = "Plan", description = "여행 계획 관리 API")
public class PlanController {
    private final TravelPlanService travelPlanService;

    // 일정 추가
    @PostMapping("/{meetingId}")
    @Operation(summary = "계획 추가", description = "모임에 새로운 여행 계획을 추가합니다")
    BaseResponse<Boolean> addPlan(
            @Parameter(description = "모임 ID") @PathVariable Long meetingId,
            @Parameter(description = "계획 생성 요청") @RequestBody PlanCreate.Request request
    ) {
        travelPlanService.createPlan(meetingId, request);
        return new BaseResponse<>("여행 계획 추가를 성공했습니다.", true);
    }

    // 세부 일정 추가
    @PostMapping("/{meetingId}/{travelPlanId}")
    @Operation(summary = "계획 항목 추가", description = "기존 여행 계획에 새로운 항목을 추가합니다")
    BaseResponse<Boolean> addPlanItem(
            @Parameter(description = "모임 ID") @PathVariable Long meetingId,
            @Parameter(description = "여행 계획 ID") @PathVariable Long travelPlanId,
            @Parameter(description = "계획 항목 생성 요청") @RequestBody PlanItemCreate.Request request
    ) {
        travelPlanService.createPlanItem(meetingId, travelPlanId, request);
        return new BaseResponse<>("세부 일정을 생성했습니다.", true);
    }

    // 세부 일정 조회
    @GetMapping("/{planItemId}")
    @Operation(summary = "계획 항목 조회", description = "특정 계획의 모든 항목을 조회합니다")
    BaseResponse<List<PlanItemResponse>> getPlanItem(
            @Parameter(description = "계획 항목 ID") @PathVariable Long planItemId
    ){
        List<PlanItemResponse> planItemResponseList = travelPlanService.getPlanItemDetail(planItemId);
        return new BaseResponse<>("세부 일정 조회에 성공했습니다.", planItemResponseList);
    }

    @PutMapping("/{planItemId}")
    @Operation(summary = "계획 항목 삭제", description = "계획에서 특정 항목을 삭제합니다")
    BaseResponse<Boolean> deletePlanItem(
            @Parameter(description = "삭제할 계획 항목 ID") @PathVariable Long planItemId
    ){
        travelPlanService.deletePlanItem(planItemId);
        return new BaseResponse<>("세부 일정 삭제에 성공했습니다", true);
    }
}
