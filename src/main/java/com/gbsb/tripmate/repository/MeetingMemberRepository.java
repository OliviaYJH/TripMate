package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.MeetingMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingMemberRepository extends JpaRepository<MeetingMemberEntity, Long> {
}
