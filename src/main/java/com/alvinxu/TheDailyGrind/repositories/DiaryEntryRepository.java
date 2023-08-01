package com.alvinxu.TheDailyGrind.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alvinxu.TheDailyGrind.models.DiaryEntry;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
	@Query(value = "SELECT * FROM diary_entry de WHERE de.account_id = :account_id"
	    + " ORDER BY de.date_of_entry DESC",
		   nativeQuery=true)
	Page<DiaryEntry> findAllByAccountId(
	    @Param("account_id") Long long1,
	    Pageable pageable
	);
	
	@Query(value = "SELECT * FROM diary_entry de"
      + " WHERE de.account_id = :account_id"
        + " AND de.date_of_entry <= :maximum_date"
      + " ORDER BY de.date_of_entry DESC",
       nativeQuery=true)
  Page<DiaryEntry> findAllOfAccountBeforeDate(
      @Param("account_id") Long accountId,
      @Param("maximum_date") LocalDateTime originDate,
      Pageable pageable
  );
	
	@Query(value = "SELECT * FROM diary_entry de"
      + " WHERE de.account_id = :account_id"
        + " AND de.date_of_entry >= :start_date"
        + " AND de.date_of_entry < :end_date"
      + " ORDER BY de.date_of_entry ASC",
       nativeQuery=true)
  Page<DiaryEntry> findAllOfAccountBetweenDates(
      @Param("account_id") Long accountId,
      @Param("start_date") LocalDateTime startDate,
      @Param("end_date") LocalDateTime endDate,
      Pageable pageable
  );
}
