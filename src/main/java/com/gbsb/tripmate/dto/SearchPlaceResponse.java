package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.entity.Place;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchPlaceResponse {
    private List<Place> places;
    private Long totalCount;
    private Long pageableCount;
    private boolean isEnd;

    public SearchPlaceResponse(List<Place> places, Long totalCount, Long pageableCount, boolean isEnd) {
        this.places = places;
        this.totalCount = totalCount;
        this.pageableCount = pageableCount;
        this.isEnd = isEnd;
    }
}
