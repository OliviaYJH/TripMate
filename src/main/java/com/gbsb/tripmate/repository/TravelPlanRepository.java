package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {

    List<TravelPlan> findAllByMeetingAndIsDeletedFalse(Meeting meeting);

    @Query("SELECT MIN(tp.planDate) FROM TravelPlan tp WHERE tp.meeting.meetingId IN "
            + "(SELECT mm.meeting.meetingId FROM MeetingMember mm WHERE mm.user.Id = :Id) "
            + "AND tp.planDate > CURRENT_DATE AND tp.isDeleted = false")
    LocalDate findNextTravelDateByUserId(@Param("Id") Long Id);
}
