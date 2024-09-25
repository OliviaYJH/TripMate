package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.PlanCreate;
import com.gbsb.tripmate.dto.PlanItemCreate;
import com.gbsb.tripmate.entity.Plan;
import com.gbsb.tripmate.entity.PlanItem;
import com.gbsb.tripmate.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    // 일정 추가
    @PostMapping("/{meetingId}")
    BaseResponse<Boolean> addPlan(
            @PathVariable Long meetingId,
            @RequestBody PlanCreate.Request request
    ) {
        planService.createPlan(meetingId, request);
        return new BaseResponse<>("여행 계획 추가를 성공했습니다.", true);
    }

    // 세부 일정 추가
//    @PostMapping("/{meetingId}")
//    BaseResponse<PlanItem> addPlanItem(
//            @PathVariable Long meetingId,
//            @PathVariable Long travelPlanId,
//            @RequestBody PlanItemCreate.Request request
//    ) {
//        planService.createPlanItem(meetingId, travelPlanId, request);
//        return new BaseResponse<>("세부 일정을 생성했습니다.", null);
//    }
}
