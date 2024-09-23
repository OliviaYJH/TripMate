package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.PlacePageResponse;
import com.gbsb.tripmate.entity.Place;
import com.gbsb.tripmate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/places")
    BaseResponse<PlacePageResponse> searchPlace(
            @RequestParam String placeName,
            @RequestParam int page,
            @RequestParam int size
    ) {
        PlacePageResponse placeList = placeService.searchPlace(placeName, page, size);
        return new BaseResponse<>("장소 검색 성공", placeList);
    }

}
