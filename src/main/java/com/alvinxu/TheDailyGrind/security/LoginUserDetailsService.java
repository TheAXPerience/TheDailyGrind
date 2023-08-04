package com.alvinxu.TheDailyGrind.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;

@Service
public class LoginUserDetailsService implements UserDetailsService {
	private AccountRepository accountRepository;
	
	// constructor injection
	public LoginUserDetailsService(AccountRepository accountRepository) {
	  super();
	  this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account user = accountRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Error: email not associated with an account.");
		}

		UserDetails userDetails = User.withUsername(user.getEmail())
				.password(user.getPassword())
				.authorities("USER") /*
				.accountLocked(false)
				.accountExpired(false)
				.credentialsExpired(false)
				.disabled(false) */
				.build();
		return userDetails;
	}
}
