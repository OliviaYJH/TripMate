package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.PlanCreate;
import com.gbsb.tripmate.dto.PlanItemCreate;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.Plan;
import com.gbsb.tripmate.entity.PlanItem;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.PlanItemRepository;
import com.gbsb.tripmate.repository.PlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanItemRepository planItemRepository;
    private final MeetingRepository meetingRepository;

    public void createPlan(Long meetingId, PlanCreate.Request request) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        Plan plan = Plan.builder()
                .meeting(meeting)
                .planTitle(request.getPlanTitle())
                .planDate(request.getPlanDate())
                .build();
        planRepository.save(plan);
    }

    public PlanItem createPlanItem(Long meetingId, Long travelPlanId, PlanItemCreate.Request request) {
        // 해당 meetingId List<Plan> 가져오기
        List<Plan> planList = planRepository.findAllByMeeting(meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT)));
        System.out.println("planList = " + planList);

        // placeId에 해당하는 장소 추가해놓기

        // 여기에 travelDate와 일치하는 Plan이 있으면 meetingId 가져오기
        // 없으면 Plan에 추가하고 meetingId 가져오기

        // meetingId 없을 때 예외처리

        ////

        // 시작 시간이 null이면 예외처리
        // placeId 유효성 처리

        // placeId에 해당하는 x, y값 가져오기

        // 현재 해당 meetingId에 해당하는 List<PlanItem>의 size() 가져와서
        // 없으면 order이 1
        // 해당 meetingId PlanItem에 추가하기


        return null;
    }
}
