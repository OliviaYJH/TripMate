package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.DailyParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyParticipationRepository extends JpaRepository<DailyParticipation, Long> {
    @Query("SELECT dp.participationDate FROM DailyParticipation dp WHERE dp.user.id = :id")
    List<LocalDate> findParticipationDatesByUserId(Long userId);
}
