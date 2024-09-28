package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.PlacePageResponse;
import com.gbsb.tripmate.dto.SearchPlaceWithKeywordResponse;
import com.gbsb.tripmate.entity.Place;
import com.gbsb.tripmate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;

    // 장소 검색
    @GetMapping("/search")
    BaseResponse<PlacePageResponse> searchPlace(
            @RequestParam String placeName,
            @RequestParam int page,
            @RequestParam int size
    ) {
        PlacePageResponse placeList = placeService.searchPlace(placeName, page, size);
        return new BaseResponse<>("장소 검색 성공", placeList);
    }

    // 장소 상세 정보 조회
    @GetMapping
    BaseResponse<Place> getPlaceDetail(
            @RequestParam Long placeId
    ) {
        Place place = placeService.getPlaceDetail(placeId);
        return new BaseResponse<>("장소 상세 정보 조회 성공", place);
    }

    // 맛집 추천 api
    @GetMapping("/restaurant")
    BaseResponse<SearchPlaceWithKeywordResponse> getRestaurant(
            @RequestParam Long placeId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        SearchPlaceWithKeywordResponse placePageResponse = placeService.getRestaurant(placeId, page, size);
        return new BaseResponse<>("맛집 정보 조회 성공", placePageResponse);
    }

    // 관광지 추천 api

}
