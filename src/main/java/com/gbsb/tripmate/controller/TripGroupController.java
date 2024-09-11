package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.UpdateTripGroup;
import com.gbsb.tripmate.service.TripGroupService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class TripGroupController {
    private final TripGroupService tripGroupService;

    public TripGroupController(TripGroupService tripGroupService) {
        this.tripGroupService = tripGroupService;
    }

    @PutMapping("/{groupId}")
    BaseResponse<Boolean> updateTripGroup(
            @PathVariable Long groupId,
            @RequestBody UpdateTripGroup.Request request
    ) {
        System.out.println("groupId " + groupId.toString());
        tripGroupService.updateTripGroup(groupId, request);
        return new BaseResponse<>("모임 수정 성공", true);
    }
}