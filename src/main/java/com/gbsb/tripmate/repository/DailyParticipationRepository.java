package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.DailyParticipationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyParticipationRepository extends JpaRepository<DailyParticipationEntity, Long> {
}
