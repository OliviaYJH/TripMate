package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {
    List<TravelPlan> findAllByMeeting(Meeting meeting);
}
