package com.alvinxu.TheDailyGrind.services;

import java.nio.CharBuffer;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alvinxu.TheDailyGrind.dto.RegisterDto;
import com.alvinxu.TheDailyGrind.exceptions.UsernameAlreadyExistsException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;

@Service
public class AccountService {
	private AccountRepository accountRepository;
	private PasswordEncoder passwordEncoder;
	
	public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
	  this.accountRepository = accountRepository;
	  this.passwordEncoder = passwordEncoder;
	}
	
	public Account getAccountById(Long id) {
	  Optional<Account> account = this.accountRepository.findById(id);
	  if (account.isEmpty()) {
	    return null;
	  }
	  return account.get();
	}
	
	public Account getAccountByEmail(String email) {
		return accountRepository.findByEmail(email);
	}
	
	public Page<Account> getAccountsLikeUsername(String username, int page, int size) {
	  Pageable pageable = PageRequest.of(page, size);
		return accountRepository.findSimilarToUsername(username.toLowerCase(), pageable);
	}
	
	public void registerNewAccount(RegisterDto registerForm) throws UsernameAlreadyExistsException {
		if (getAccountByEmail(registerForm.getEmail()) != null) {
			throw new UsernameAlreadyExistsException("Error: email is already associated with an account.");
		}
		
		Account user = new Account();
		user.setEmail(registerForm.getEmail());
		user.setUsername(registerForm.getUsername());
		user.setPassword(
		    passwordEncoder.encode(CharBuffer.wrap(registerForm.getPassword()))
		);
		user.setAuthority("USER");
		user.setDateOfBirth(registerForm.getDateOfBirth().atStartOfDay());
		accountRepository.save(user);
	}
}
