package com.alvinxu.TheDailyGrind.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alvinxu.TheDailyGrind.models.DiaryEntry;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
	@Query(value = "SELECT * FROM diary_entry de WHERE de.account_id = :account_id",
		   nativeQuery=true)
	List<DiaryEntry> findAllByAccountId(@Param("account_id") Long long1);
}
