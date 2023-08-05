package com.alvinxu.TheDailyGrind.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.services.AccountService;

@ExtendWith(MockitoExtension.class)
public class TestEmailValidator {
  @Mock private AccountService service;
  private EmailValidator validator;
  
  @BeforeEach
  void setup() {
    validator = new EmailValidator(service);
  }
  
  @Test
  void testIsValid() {
    String email = "hello@world";
    Mockito.when(service.getAccountByEmail(email)).thenReturn(null);
    
    boolean expected = validator.isValid(email, null);
    ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
    verify(service).getAccountByEmail(emailCaptor.capture());
    assertThat(expected).isTrue();
    assertThat(emailCaptor.getValue()).isEqualTo(email);
  }
  
  @Test
  void testIsInvalid() {
    String email = "hello@world";
    Mockito.when(service.getAccountByEmail(email)).thenReturn(new Account());
    
    boolean expected = validator.isValid(email, null);
    ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
    verify(service).getAccountByEmail(emailCaptor.capture());
    assertThat(expected).isFalse();
    assertThat(emailCaptor.getValue()).isEqualTo(email);
  }
}
