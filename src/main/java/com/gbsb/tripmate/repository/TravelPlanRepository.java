package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {

    @Query("SELECT MIN(tp.planDate) FROM TravelPlan tp WHERE tp.meeting.meetingId IN "
            + "(SELECT mm.meeting.meetingId FROM MeetingMember mm WHERE mm.user.Id = :Id) "
            + "AND tp.planDate > CURRENT_DATE")
    LocalDate findNextTravelDateByUserId(@Param("Id") Long Id);
}
