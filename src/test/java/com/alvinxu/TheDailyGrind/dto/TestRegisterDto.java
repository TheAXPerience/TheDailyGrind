package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TestRegisterDto {
  @Test
  void testEmail() {
    RegisterDto dto = new RegisterDto();
    String email = "hello@world";
    
    dto.setEmail(email);
    assertThat(dto.getEmail()).isEqualTo(email);
  }
  
  @Test
  void testUsername() {
    RegisterDto dto = new RegisterDto();
    String username = "username";
    
    dto.setUsername(username);
    assertThat(dto.getUsername()).isEqualTo(username);
  }

  @Test
  void testPassword() {
    RegisterDto dto = new RegisterDto();
    char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    
    dto.setPassword(password);
    assertThat(dto.getPassword()).isEqualTo(password);
  }
  
  @Test
  void testDateOfBirth() {
    RegisterDto dto = new RegisterDto();
    LocalDate date = LocalDate.of(1994, 6, 30);
    
    dto.setDateOfBirth(date);
    assertThat(dto.getDateOfBirth()).isEqualTo(date);
  }
  
  @Test
  void testTermsOfServiceAgreement() {
    RegisterDto dto = new RegisterDto();
    
    dto.setTos_agree(false);
    assertThat(dto.isTos_agree()).isFalse();
    
    dto.setTos_agree(true);
    assertThat(dto.isTos_agree()).isTrue();
  }
}
