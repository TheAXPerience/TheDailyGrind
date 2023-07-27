package com.alvinxu.TheDailyGrind.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alvinxu.TheDailyGrind.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	public Account findByEmail(String email);
	
	@Query(value="SELECT * FROM account a WHERE a.username LIKE :username"
	    + " ORDER BY LOCATE(:username, a.username), a.username ASC",
	    nativeQuery=true
	)
	public List<Account> findSimilarToUsername(String username);
}
