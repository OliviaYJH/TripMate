package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlacePageResponse {
    private List<Place> places;
    private int currentPage; // 현재 페이지 번호
    private int totalPages; // 전체 페이지 수
    private long totalElements; // 전체 장소 수
    private boolean hasNext; // 다음 페이지 여부
}
