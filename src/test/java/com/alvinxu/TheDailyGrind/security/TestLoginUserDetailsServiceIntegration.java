package com.alvinxu.TheDailyGrind.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;

@DataJpaTest
public class TestLoginUserDetailsServiceIntegration {
  @Autowired private AccountRepository accountRepository;
  private LoginUserDetailsService underTest;
  
  @BeforeEach
  void setup() {
    underTest = new LoginUserDetailsService(accountRepository);
  }
  
  @AfterEach
  void teardown() {
    accountRepository.deleteAll();
  }
  
  @Test
  void testLoadUserByUsername() {
    // given
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accountRepository.save(user);
    
    // when
    UserDetails ud = underTest.loadUserByUsername(email);
    
    // then
    assertThat(ud.getUsername()).isEqualTo(email);
    assertThat(ud.getPassword()).isEqualTo(user.getPassword());
  }
  
  @Test
  void testLoadUserByUsernameInvalidEmail() {
 // given
    String email = "hello@world";
    
    // then
    assertThatThrownBy(() -> underTest.loadUserByUsername(email))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("Error: email not associated with an account.");
  }
}
