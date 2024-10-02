package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.RouteResponse;
import com.gbsb.tripmate.entity.TravelPlan;
import com.gbsb.tripmate.entity.PlanItem;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.TravelPlanRepository;
import com.gbsb.tripmate.repository.PlanItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private static final String KAKAO_MOBILITY_API_URL = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";

    @Value("${kakaomap.key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate;
    private final TravelPlanRepository travelPlanRepository;
    private final PlanItemRepository planItemRepository;

    public RouteService(RestTemplate restTemplate, TravelPlanRepository travelPlanRepository, PlanItemRepository planItemRepository) {
        this.restTemplate = restTemplate;
        this.travelPlanRepository = travelPlanRepository;
        this.planItemRepository = planItemRepository;
    }

    public RouteResponse findRouteByTravelPlanId(Long travelPlanId) {
        TravelPlan travelPlan = travelPlanRepository.findById(travelPlanId)
                .orElseThrow(() -> new MeetingException(ErrorCode.PLAN_NOT_FOUND));

        List<PlanItem> planItems = planItemRepository.findByTravelPlanAndIsDeletedFalseOrderByStartTimeAsc(travelPlan);

        if (planItems.size() < 2) {
            throw new MeetingException(ErrorCode.INSUFFICIENT_PLAN_ITEMS);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

        Map<String, Object> requestBody = createRequestBody(planItems);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<RouteResponse> response = restTemplate.exchange(
                    KAKAO_MOBILITY_API_URL,
                    HttpMethod.POST,
                    entity,
                    RouteResponse.class);

            return response.getBody();
        } catch (Exception e) {
            throw new MeetingException(ErrorCode.KAKAO_API_ERROR);
        }
    }

    private Map<String, Object> createRequestBody(List<PlanItem> planItems) {
        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put("origin", createCoordinate(planItems.get(0)));
        requestBody.put("destination", createCoordinate(planItems.get(planItems.size() - 1)));

        if (planItems.size() > 2) {
            List<Map<String, Object>> waypoints = planItems.subList(1, planItems.size() - 1).stream()
                    .map(this::createCoordinate)
                    .collect(Collectors.toList());
            requestBody.put("waypoints", waypoints);
        }

        requestBody.put("priority", "RECOMMEND");
        requestBody.put("car_fuel", "GASOLINE");
        requestBody.put("car_hipass", false);
        requestBody.put("alternatives", false);
        requestBody.put("road_details", false);

        return requestBody;
    }

    private Map<String, Object> createCoordinate(PlanItem planItem) {
        Map<String, Object> coordinate = new HashMap<>();
        coordinate.put("name", planItem.getItemName());
        coordinate.put("x", String.valueOf(planItem.getX()));
        coordinate.put("y", String.valueOf(planItem.getY()));
        return coordinate;
    }
}