package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.MeetingMember;
import com.gbsb.tripmate.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    // 멤버 수 조회
    @Query("SELECT COUNT(mm) FROM MeetingMember mm WHERE mm.meeting.meetingId = :meetingId")
    int countMembersByGroupId(@Param("meetingId") Long meetingId);

    Optional<MeetingMember> findByMeetingAndUser(Meeting meeting, User user);
    Optional<MeetingMember> findByMeetingAndUserId(Meeting meeting, Long userId);
    boolean existsByMeetingAndUser(Meeting meeting, User user);
}
