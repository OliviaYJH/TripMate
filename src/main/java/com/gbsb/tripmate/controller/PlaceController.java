package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.entity.Place;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

import static com.gbsb.tripmate.enums.ErrorCode.FAIL_ENCODING;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/places")
    BaseResponse<Place> searchPlace(
            @RequestParam String address
    ) {
        Place place = null;
        place = placeService.getPlaceFromApi(address);
        return new BaseResponse<>("장소 검색 성공", place);
    }

}
