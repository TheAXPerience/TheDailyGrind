package com.alvinxu.TheDailyGrind.services;

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
	
	public Account getAccountByEmail(String email) {
		return accountRepository.findByEmail(email);
	}
	
	public boolean registerNewAccount(RegisterDto registerForm) {
		if (getAccountByEmail(registerForm.getEmail()) != null) {
			return false;
		}
		
		Account user = new Account();
		user.setEmail(registerForm.getEmail());
		user.setUsername(registerForm.getUsername());
		// TODO: password encoding
		user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
		accountRepository.save(user);
		
		return true;
	}
}