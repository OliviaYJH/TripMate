package com.gbsb.tripmate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private String transId;
    private List<Route> routes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Route {
        private int resultCode;
        private String resultMsg;
        private Summary summary;
        private List<Section> sections;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private Coordinate origin;
        private Coordinate destination;
        private List<Coordinate> waypoints;
        private String priority;
        private Bound bound;
        private Fare fare;
        private int distance;
        private int duration;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinate {
        private String name;
        private Double x;
        private Double y;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bound {
        private Double minX;
        private Double minY;
        private Double maxX;
        private Double maxY;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fare {
        private int taxi;
        private int toll;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Section {
        private int distance;
        private int duration;
        private Bound bound;
        private List<Road> roads;
        private List<Guide> guides;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Road {
        private String name;
        private int distance;
        private int duration;
        private double trafficSpeed;
        private int trafficState;
        private List<Double> vertexes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Guide {
        private String name;
        private Double x;
        private Double y;
        private int distance;
        private int duration;
        private int type;
        private String guidance;
        private int roadIndex;
    }
}