package com.alvinxu.TheDailyGrind.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alvinxu.TheDailyGrind.models.CalendarEvent;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
	
	@Query(value = "SELECT * FROM calendar_event ce"
			+ " WHERE ce.account_id = :account_id"
			+ " AND (ce.is_public OR :is_user)"
			+ " ORDER BY ce.date_of_event",
		   nativeQuery=true)
	Page<CalendarEvent> findAllByAccountId(
			@Param("account_id") Long accountId,
			@Param("is_user") boolean isUser,
			Pageable pageable
	);
	
	@Query(value = "SELECT * FROM calendar_event ce"
			+ " WHERE ce.account_id = :account_id"
				+ " AND (ce.is_public OR :is_user)"
				+ " AND ce.date_of_event >= :minimum_date"
			+ " ORDER BY ce.date_of_event",
		   nativeQuery=true)
	Page<CalendarEvent> findAllOfAccountAfterDate(
			@Param("account_id") Long accountId,
			@Param("is_user") boolean isUser,
			@Param("minimum_date") LocalDateTime originDate,
			Pageable pageable
	);
	
	@Query(value = "SELECT * FROM calendar_event ce"
			+ " WHERE ce.account_id = :account_id"
				+ " AND (ce.is_public OR :is_user)"
				+ " AND ce.date_of_event >= :start_date"
				+ " AND ce.date_of_event <= :end_date"
			+ " ORDER BY ce.date_of_event",
		   nativeQuery=true)
	Page<CalendarEvent> findAllOfAccountBetweenDates(
			@Param("account_id") Long accountId,
			@Param("is_user") boolean isUser,
			@Param("start_date") LocalDateTime startDate,
			@Param("end_date") LocalDateTime endDate,
			Pageable pageable
	);
}
