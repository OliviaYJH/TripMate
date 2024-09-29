package com.gbsb.tripmate.repository;

import com.gbsb.tripmate.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByMeetingMeetingId(Long meetingId);
    List<Expense> findByMeetingMeetingIdAndIsGroupExpense(Long meetingId, Boolean isGroupExpense);
}
