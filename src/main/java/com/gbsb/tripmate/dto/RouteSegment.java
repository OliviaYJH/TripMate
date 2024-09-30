package com.gbsb.tripmate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteSegment {
    private String startName;
    private String endName;
    private int duration;
    private int distance;
}
