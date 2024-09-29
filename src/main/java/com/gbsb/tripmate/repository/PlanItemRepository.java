package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.TravelPlan;
import com.gbsb.tripmate.entity.PlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanItemRepository extends JpaRepository<PlanItem, Long> {
    List<PlanItem> findByTravelPlanAndIsDeletedFalseOrderByStartTimeAsc(TravelPlan travelPlan);
    List<PlanItem> findAllByTravelPlanAndIsDeletedFalseOrderByItemOrderAsc(TravelPlan travelPlan);
    Optional<PlanItem> findByPlanItemIdAndIsDeletedFalse(Long travelPlanId);
}
