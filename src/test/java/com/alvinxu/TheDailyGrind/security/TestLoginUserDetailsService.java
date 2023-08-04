package com.alvinxu.TheDailyGrind.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class TestLoginUserDetailsService {
  @Mock private AccountRepository accountRepository;
  private LoginUserDetailsService underTest;
  
  @BeforeEach
  void setUp() {
    underTest = new LoginUserDetailsService(accountRepository);
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
    Mockito.when(accountRepository.findByEmail(email)).thenReturn(user);
    
    // when
    UserDetails ud = underTest.loadUserByUsername(email);
    
    // then
    ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
    
    verify(accountRepository).findByEmail(emailArgumentCaptor.capture());
    
    assertThat(emailArgumentCaptor.getValue()).isEqualTo(email);
    assertThat(ud.getUsername()).isEqualTo(email);
    assertThat(ud.getPassword()).isEqualTo(user.getPassword());
  }
  
  @Test
  void testLoadUserByUserNameWithUnexistentUser() {
    // given
    String email = "hello@world";
    Mockito.when(accountRepository.findByEmail(email)).thenReturn(null);
    // given(accountRepository.findByEmail(email)).willReturn(null);
    
    // then
    assertThatThrownBy(() -> underTest.loadUserByUsername(email))
        .isInstanceOf(UsernameNotFoundException.class)
        .hasMessageContaining("Error: email not associated with an account.");
  }
}
