package com.alvinxu.TheDailyGrind.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alvinxu.TheDailyGrind.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	public Account findByEmail(String email);
}
