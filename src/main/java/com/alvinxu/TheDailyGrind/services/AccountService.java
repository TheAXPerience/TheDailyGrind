package com.alvinxu.TheDailyGrind.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alvinxu.TheDailyGrind.dto.RegisterDto;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;

@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Account getAccountById(Long id) {
	  Optional<Account> account = this.accountRepository.findById(id);
	  if (account.isEmpty()) {
	    throw new IllegalArgumentException("Error: could not find user");
	  }
	  return account.get();
	}
	
	public Account getAccountByEmail(String email) {
		return accountRepository.findByEmail(email);
	}
	
	// TODO
	public List<Account> getAccountsLikeUsername(String username) {
		return accountRepository.findSimilarToUsername("%" + username + "%");
	}
	
	public boolean registerNewAccount(RegisterDto registerForm) {
		if (getAccountByEmail(registerForm.getEmail()) != null) {
			return false;
		}
		
		Account user = new Account();
		user.setEmail(registerForm.getEmail());
		user.setUsername(registerForm.getUsername());
		user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
		user.setAuthority("USER");
		user.setDateOfBirth(registerForm.getDate_of_birth());
		accountRepository.save(user);
		
		return true;
	}
}
