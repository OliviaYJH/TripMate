package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.PlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanItemRepository extends JpaRepository<PlanItem, Long> {
}
