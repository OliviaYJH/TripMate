package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {


    @Query("SELECT m FROM Meeting m WHERE m.meetingLeader.userId = :userId OR m IN (SELECT mm.meeting FROM MeetingMember mm WHERE mm.user.userId = :userId)")
    Page<Meeting> findMeetingsByUserId(@Param("userId") Long userId, Pageable pageable);
    Page<Meeting> findAll(Pageable pageable);
}