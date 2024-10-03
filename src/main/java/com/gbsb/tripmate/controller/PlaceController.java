package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.PlacePageResponse;
import com.gbsb.tripmate.dto.SearchPlaceWithKeywordResponse;
import com.gbsb.tripmate.entity.Place;
import com.gbsb.tripmate.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
@Tag(name = "Place", description = "장소 관련 API")
public class PlaceController {
    private final PlaceService placeService;

    // 장소 검색
    @GetMapping("/search")
    @Operation(summary = "장소 검색", description = "주어진 이름으로 장소를 검색합니다")
    BaseResponse<PlacePageResponse> searchPlace(
            @Parameter(description = "검색할 장소 이름") @RequestParam String placeName,
            @Parameter(description = "페이지 번호") @RequestParam int page,
            @Parameter(description = "페이지당 항목 수") @RequestParam int size
    ) {
        PlacePageResponse placeList = placeService.searchPlace(placeName, page, size);
        return new BaseResponse<>("장소 검색 성공", placeList);
    }

    // 장소 상세 정보 조회
    @GetMapping
    @Operation(summary = "장소 상세 정보 조회", description = "특정 장소의 상세 정보를 조회합니다")
    BaseResponse<Place> getPlaceDetail(
            @Parameter(description = "장소 ID") @RequestParam Long placeId
    ) {
        Place place = placeService.getPlaceDetail(placeId);
        return new BaseResponse<>("장소 상세 정보 조회 성공", place);
    }

    // 맛집 추천 api
    @GetMapping("/restaurant")
    @Operation(summary = "맛집 추천", description = "특정 장소 근처의 음식점을 추천합니다")
    BaseResponse<SearchPlaceWithKeywordResponse> getRestaurant(
            @Parameter(description = "기준 장소 ID") @RequestParam Long placeId,
            @Parameter(description = "페이지 번호") @RequestParam int page,
            @Parameter(description = "페이지당 항목 수") @RequestParam int size
    ) {
        SearchPlaceWithKeywordResponse placePageResponse = placeService.getRestaurant(placeId, page, size);
        return new BaseResponse<>("맛집 정보 조회 성공", placePageResponse);
    }

    // 명소 추천 api
    @GetMapping("/attraction")
    @Operation(summary = "관광 명소 추천", description = "특정 장소 근처의 관광 명소를 추천합니다")
    BaseResponse<SearchPlaceWithKeywordResponse> getAttraction(
            @Parameter(description = "기준 장소 ID") @RequestParam Long placeId,
            @Parameter(description = "페이지 번호") @RequestParam int page,
            @Parameter(description = "페이지당 항목 수") @RequestParam int size
    ) {
        SearchPlaceWithKeywordResponse placePageResponse = placeService.getAttraction(placeId, page, size);
        return new BaseResponse<>("명소 정보 조회 성공", placePageResponse);
    }
}
