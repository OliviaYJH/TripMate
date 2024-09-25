package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByMeeting(Meeting meeting);
}
