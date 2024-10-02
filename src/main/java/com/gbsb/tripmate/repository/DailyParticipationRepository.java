package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.DailyParticipation;
import com.gbsb.tripmate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyParticipationRepository extends JpaRepository<DailyParticipation, Long> {
    @Query("SELECT dp.participationDate FROM DailyParticipation dp WHERE dp.user.id = :id AND dp.isDeleted = false")
    List<LocalDate> findParticipationDatesById(Long id);

    Optional<DailyParticipation> findByUserAndParticipationDateAndIsDeletedFalse(User user, LocalDate participationDate);
}
