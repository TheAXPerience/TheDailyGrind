package com.alvinxu.TheDailyGrind.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alvinxu.TheDailyGrind.models.CalendarEvent;
import com.alvinxu.TheDailyGrind.models.DiaryEntry;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
	@Query(value = "SELECT * FROM diary_entry de WHERE de.account_id = :account_id"
	    + " ORDER BY de.date_of_entry DESC",
		   nativeQuery=true)
	List<DiaryEntry> findAllByAccountId(@Param("account_id") Long long1);
	
	@Query(value = "SELECT * FROM diary_entry de"
      + " WHERE de.account_id = :account_id"
        + " AND de.date_of_entry <= :maximum_date"
      + " ORDER BY de.date_of_entry DESC",
       nativeQuery=true)
  List<DiaryEntry> findAllOfAccountBeforeDate(
      @Param("account_id") Long accountId,
      @Param("maximum_date") LocalDateTime originDate
  );
	
	@Query(value = "SELECT * FROM diary_entry de"
      + " WHERE de.account_id = :account_id"
        + " AND de.date_of_entry >= :start_date"
        + " AND de.date_of_entry < :end_date"
      + " ORDER BY de.date_of_entry ASC",
       nativeQuery=true)
  List<DiaryEntry> findAllOfAccountBetweenDates(
      @Param("account_id") Long accountId,
      @Param("start_date") LocalDateTime startDate,
      @Param("end_date") LocalDateTime endDate
  );
}
