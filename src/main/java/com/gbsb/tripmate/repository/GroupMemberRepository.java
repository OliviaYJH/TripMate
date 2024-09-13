package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.GroupMember;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // 멤버 수 조회
    @Query("SELECT COUNT(gm) FROM GroupMember gm WHERE gm.group.groupId = :groupId")
    int countMembersByGroupId(@Param("groupId") Long groupId);

    // 모임에 참여한 모든 멤버 조회
    List<GroupMember> findByGroup_GroupId(Long groupId);
}
