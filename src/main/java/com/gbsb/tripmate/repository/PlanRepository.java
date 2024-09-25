package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByMeeting(Meeting meeting);
}
