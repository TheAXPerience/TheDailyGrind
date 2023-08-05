package com.alvinxu.TheDailyGrind.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;

@DataJpaTest
public class TestRegisterDtoValidation {
  @Autowired AccountRepository repository;
  private RegisterDto dto;
  
  @BeforeEach
  void setup() {
    dto = new RegisterDto();
    dto.setEmail("hello@world");
    dto.setUsername("My user");
    dto.setPassword(new char[] {'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1'});
    dto.setDateOfBirth(LocalDate.of(2012, 6, 9));
    dto.setTos_agree(true);
  }
  
  @AfterEach
  void teardown() {
    repository.deleteAll();
  }
  
  @Test
  void testValidation() {
    
  }
  
  @Test
  void testEmailNotEmpty() {
    dto.setEmail("");
  }
  
  @Test
  void testEmailIsValidEmail() {
    dto.setEmail("hello world");
  }
  
  @Test
  void testEmailValidator() {
    Account user = new Account();
    user.setAuthority("USER");
    user.setPassword("password");
    user.setUsername("username");
    user.setDateOfBirth(LocalDateTime.of(2014, 5, 30, 16, 07));
    user.setEmail("hello@world");
    repository.save(user);
  }
  
  @Test
  void testUsernameSizeTooSmall() {
    dto.setUsername("us");
  }
  
  @Test
  void testUsernameSizeTooLarge() {
    dto.setUsername("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
  }
  
  @Test
  void testPasswordValidator() {
    dto.setPassword(new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'});
  }
  
  @Test
  void testPasswordSizeTooSmall() {
    dto.setPassword(new char[] {'P', 'a', 's', 's', '1', '2', '3'});
  }
  
  @Test
  void testPasswordSizeTooLarge() {
    dto.setPassword("Pass12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890".toCharArray());
  }
  
  @Test
  void testDateOfBirthInTheFuture() {
    dto.setDateOfBirth(LocalDate.of(4000, 12, 31));
  }
  
  @Test
  void testDateOfBirthNotNull() {
    dto.setDateOfBirth(null);
  }
  
  @Test
  void testTermsOfServiceNotAgreed() {
    dto.setTos_agree(false);
  }
}
