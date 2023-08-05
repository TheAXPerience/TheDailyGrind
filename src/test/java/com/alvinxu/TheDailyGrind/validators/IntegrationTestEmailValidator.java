package com.alvinxu.TheDailyGrind.validators;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;
import com.alvinxu.TheDailyGrind.services.AccountService;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTestEmailValidator {
  @Autowired private AccountRepository repository;
  private AccountService service;
  private EmailValidator validator;
  
  @BeforeAll
  void setup() {
    service = new AccountService(repository, new BCryptPasswordEncoder());
    validator = new EmailValidator(service);
  }
  
  @Test
  void testIsValid() {
    String email = "hello@world";
    
    boolean expected = validator.isValid(email, null);
    assertThat(expected).isTrue();
  }
  
  @Test
  void testIsInvalid() {
    Account user = new Account();
    user.setEmail("hello@world");
    user.setUsername("username");
    user.setPassword("password");
    user.setDateOfBirth(LocalDateTime.of(2012, 3, 13, 6, 17));
    user.setAuthority("USER");
    repository.save(user);
    
    String email = "hello@world";
    
    boolean expected = validator.isValid(email, null);
    assertThat(expected).isFalse();
  }
}
