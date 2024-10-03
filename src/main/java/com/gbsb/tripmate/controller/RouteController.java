package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.RouteResponse;
import com.gbsb.tripmate.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
@Tag(name = "Route", description = "경로 관련 API")
public class RouteController {
    private final RouteService routeService;

    @PostMapping("/find/{travelPlanId}")
    @Operation(summary = "경로 찾기", description = "여행 계획에 대한 최적 경로를 찾습니다.")
    public BaseResponse<RouteResponse> findRoute(
            @Parameter(description = "여행 계획 ID", required = true)
            @PathVariable Long travelPlanId) {
        RouteResponse routeResponse = routeService.findRouteByTravelPlanId(travelPlanId);
        return new BaseResponse<>("경로 찾기 성공", routeResponse);
    }
}