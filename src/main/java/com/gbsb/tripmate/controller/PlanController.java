package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.PlanCreate;
import com.gbsb.tripmate.dto.PlanItemCreate;
import com.gbsb.tripmate.dto.PlanItemResponse;
import com.gbsb.tripmate.entity.PlanItem;
import com.gbsb.tripmate.service.TravelPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {
    private final TravelPlanService travelPlanService;

    // 일정 추가
    @PostMapping("/{meetingId}")
    BaseResponse<Boolean> addPlan(
            @PathVariable Long meetingId,
            @RequestBody PlanCreate.Request request
    ) {
        travelPlanService.createPlan(meetingId, request);
        return new BaseResponse<>("여행 계획 추가를 성공했습니다.", true);
    }

    // 세부 일정 추가
    @PostMapping("/{meetingId}/{travelPlanId}")
    BaseResponse<Boolean> addPlanItem(
            @PathVariable Long meetingId,
            @PathVariable Long travelPlanId,
            @RequestBody PlanItemCreate.Request request
    ) {
        travelPlanService.createPlanItem(meetingId, travelPlanId, request);
        return new BaseResponse<>("세부 일정을 생성했습니다.", true);
    }

    // 세부 일정 조회
    @GetMapping("/{planItemId}")
    BaseResponse<List<PlanItemResponse>> getPlanItem(
            @PathVariable Long planItemId
    ){
        List<PlanItemResponse> planItemResponseList = travelPlanService.getPlanItemDetail(planItemId);
        return new BaseResponse<>("세부 일정 조회에 성공했습니다.", planItemResponseList);
    }

    @PutMapping("/{planItemId}")
    BaseResponse<Boolean> deletePlanItem(
        @PathVariable Long planItemId
    ){
        travelPlanService.deletePlanItem(planItemId);
        return new BaseResponse<>("세부 일정 삭제에 성공했습니다", true);
    }
}
