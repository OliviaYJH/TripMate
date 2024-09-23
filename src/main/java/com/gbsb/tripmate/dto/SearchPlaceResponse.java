package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.entity.Place;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchPlaceResponse {
    private List<Place> places;
    private int page;
    private boolean isEnd;

    public SearchPlaceResponse(List<Place> places, int page, boolean isEnd) {
        this.places = places;
        this.page = page;
        this.isEnd = isEnd;
    }
}
