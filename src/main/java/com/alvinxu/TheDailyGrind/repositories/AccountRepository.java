package com.alvinxu.TheDailyGrind.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alvinxu.TheDailyGrind.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	public Account findByEmail(String email);
	
	@Query(value="SELECT * FROM account a WHERE a.username LIKE :username"
	    + " ORDER BY INSTR(:username, a.username) ASC, a.username ASC",
	    nativeQuery=true
	)
	public Page<Account> findSimilarToUsername(String username, Pageable pageable);
}
