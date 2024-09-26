package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.PlanCreate;
import com.gbsb.tripmate.dto.PlanItemCreate;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.Place;
import com.gbsb.tripmate.entity.TravelPlan;
import com.gbsb.tripmate.entity.PlanItem;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.PlaceRepository;
import com.gbsb.tripmate.repository.PlanItemRepository;
import com.gbsb.tripmate.repository.TravelPlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TravelPlanService {
    private final TravelPlanRepository travelPlanRepository;
    private final PlanItemRepository planItemRepository;
    private final MeetingRepository meetingRepository;
    private final PlaceRepository placeRepository;

    public void createPlan(Long meetingId, PlanCreate.Request request) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        List<TravelPlan> travelPlanList = travelPlanRepository.findAllByMeeting(meeting);
        for (TravelPlan value : travelPlanList) {
            if (value.getPlanDate().equals(request.getPlanDate())) {
                throw new MeetingException(ErrorCode.ALREADY_DATE_EXIST);
            }
        }

        TravelPlan travelPlan = TravelPlan.builder()
                .meeting(meeting)
                .planTitle(request.getPlanTitle())
                .planDate(request.getPlanDate())
                .build();
        travelPlanRepository.save(travelPlan);
    }

    public PlanItem createPlanItem(Long meetingId, Long travelPlanId, PlanItemCreate.Request request) {
        meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        TravelPlan travelPlan = travelPlanRepository.findById(travelPlanId)
                .orElseThrow(() -> new MeetingException(ErrorCode.PLAN_NOT_FOUND));

        List<PlanItem> planItemList = planItemRepository.findByTravelPlanOrderByStartTimeAsc(travelPlan);

        Place place = placeRepository.findById(request.getPlaceId())
                .orElseThrow(() -> new MeetingException(ErrorCode.PLACE_NOT_FOUND));

        int newOrder = 1; // default
        if (!planItemList.isEmpty()) {
            for (int i = 0; i < planItemList.size(); i++) {
                PlanItem currentItem = planItemList.get(i);

                // 시작 시간이 같은 여행이 있는 경우
                if (request.getStartTime().equals(currentItem.getStartTime())) {
                    throw new MeetingException(ErrorCode.ALREADY_SAME_START_TIME_EXIST);
                }

                if (request.getStartTime().isBefore(currentItem.getStartTime())) {
                    newOrder = i + 1;
                    break;
                } else {
                    newOrder = planItemList.size() + 1;
                }
            }
            for (int i = newOrder - 1; i < planItemList.size(); i++) {
                PlanItem itemUpdate = planItemList.get(i);
                itemUpdate.setItemOrder(itemUpdate.getItemOrder() + 1);
                planItemRepository.save(itemUpdate);
            }
        }

        PlanItem planItem = PlanItem.builder()
                .travelPlan(travelPlan)
                .itemName(request.getPlanItemName())
                .itemType(request.getTravelStyle())
                .itemDescription(request.getPlanItemDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .x(place.getX())
                .y(place.getY())
                .itemOrder(newOrder)
                .build();
        planItemRepository.save(planItem);
        return null;
    }
}
