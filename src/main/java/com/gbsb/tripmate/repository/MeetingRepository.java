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


    @Query("SELECT m FROM Meeting m WHERE m.meetingLeader.id = :Id OR m IN (SELECT mm.meeting FROM MeetingMember mm WHERE mm.user.id = :Id)")
    Page<Meeting> findMeetingsByUserId(@Param("Id") Long Id, Pageable pageable);
    Page<Meeting> findAll(Pageable pageable);
}