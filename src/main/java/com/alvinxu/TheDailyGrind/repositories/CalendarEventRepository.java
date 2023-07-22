package com.alvinxu.TheDailyGrind.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alvinxu.TheDailyGrind.models.CalendarEvent;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
	@Query(value = "SELECT * FROM calendar_event ce WHERE ce.account_id = :account_id",
		   nativeQuery=true)
	List<CalendarEvent> findAllByAccountId(@Param("account_id") Long long1);
}
