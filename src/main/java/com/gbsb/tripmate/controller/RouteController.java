package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.RouteResponse;
import com.gbsb.tripmate.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
public class RouteController {
    private final RouteService routeService;

    @PostMapping("/find/{travelPlanId}")
    public BaseResponse<RouteResponse> findRoute(@PathVariable Long travelPlanId) {
        RouteResponse routeResponse = routeService.findRouteByTravelPlanId(travelPlanId);
        return new BaseResponse<>("경로 찾기 성공", routeResponse);
    }
}