package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.DailyParticipationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyParticipationRepository extends JpaRepository<DailyParticipationEntity, Long> {
    @Query("SELECT dp.participationDate FROM DailyParticipationEntity dp WHERE dp.userEntity.userId = :userId")
    List<LocalDate> findParticipationDatesByUserId(Long userId);
}
