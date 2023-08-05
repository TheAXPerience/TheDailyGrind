package com.alvinxu.TheDailyGrind.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import java.nio.CharBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.alvinxu.TheDailyGrind.dto.RegisterDto;
import com.alvinxu.TheDailyGrind.exceptions.UsernameAlreadyExistsException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class TestAccountService {
  @Mock private AccountRepository accountRepository;
  @Mock private BCryptPasswordEncoder passwordEncoder;
  private AccountService underTest;
  
  @BeforeEach
  void setup() {
    underTest = new AccountService(accountRepository, passwordEncoder);
  }
  
  @Test
  void testGetAccountById() {
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(user));
    
    Account expected = underTest.getAccountById(1L);
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(accountRepository).findById(idCaptor.capture());
    assertThat(idCaptor.getValue()).isEqualTo(1L);
    assertThat(expected).isEqualTo(user);
  }
  
  @Test
  void testGetAccountByIdInvalidUser() {
    Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());
    
    Account expected = underTest.getAccountById(1L);
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(accountRepository).findById(idCaptor.capture());
    assertThat(idCaptor.getValue()).isEqualTo(1L);
    assertThat(expected).isNull();
  }
  
  @Test
  void testGetAccountByEmail() {
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    Mockito.when(accountRepository.findByEmail(email)).thenReturn(user);
    
    Account expected = underTest.getAccountByEmail(email);
    
    ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(accountRepository).findByEmail(emailArgumentCaptor.capture());
    assertThat(expected).isEqualTo(user);
  }
  
  @Test
  void testGetAccountByEmailInvalidEmail() {
    String email = "hello@world";
    Mockito.when(accountRepository.findByEmail(email)).thenReturn(null);
    
    Account expected = underTest.getAccountByEmail(email);
    
    ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(accountRepository).findByEmail(emailArgumentCaptor.capture());
    assertThat(expected).isNull();
  }
  
  @Test
  void testGetAccountsLikeUsername() {
    Account user1 = new Account();
    user1.setUsername("username");
    user1.setPassword("password");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 7, 16, 5, 40));
    user1.setAuthority("USER1");
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2013, 4, 20, 6, 9));
    user2.setAuthority("USER2");
    
    Account user3 = new Account();
    user3.setUsername("user");
    user3.setPassword("password3");
    user3.setEmail("hello@bonk");
    user3.setDateOfBirth(LocalDateTime.of(2017, 7, 7, 8, 49));
    user3.setAuthority("USER3");
    
    Pageable pageable = PageRequest.of(0, 5);
    
    Mockito.when(accountRepository.findSimilarToUsername("user", pageable)).thenReturn(
        new PageImpl<Account>(List.of(user1, user2, user3), pageable, 3)
    );
    
    Page<Account> expected = underTest.getAccountsLikeUsername("user", 0, 5);
    
    ArgumentCaptor<String> paramCapture = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Pageable> pageCapture = ArgumentCaptor.forClass(Pageable.class);
    verify(accountRepository).findSimilarToUsername(paramCapture.capture(), pageCapture.capture());
    assertThat(expected.getContent().size()).isEqualTo(3);
    assertThat(paramCapture.getValue()).isEqualTo("user");
    assertThat(pageCapture.getValue()).isEqualTo(pageable);
  }
  
  @Test
  void testRegisterNewUser() {
    RegisterDto dto = new RegisterDto();
    char[] password = {'p', 'a', 's', 's'};
    dto.setDateOfBirth(LocalDate.of(2012, 11, 13));
    dto.setEmail("hello@world");
    dto.setUsername("username");
    dto.setPassword(password);
    dto.setTos_agree(true);
    
    Mockito.when(passwordEncoder.encode(CharBuffer.wrap(password)))
           .thenReturn("password");
    Mockito.when(accountRepository.findByEmail("hello@world"))
           .thenReturn(null);
    
    underTest.registerNewAccount(dto);
    
    ArgumentCaptor<CharSequence> passwordCaptor = ArgumentCaptor.forClass(CharSequence.class);
    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
    
    verify(accountRepository).findByEmail(emailCaptor.capture());
    assertThat(emailCaptor.getValue()).isEqualTo("hello@world");
    
    verify(passwordEncoder).encode(passwordCaptor.capture());
    assertThat(passwordCaptor.getValue()).isEqualTo(CharBuffer.wrap(password));
    
    verify(accountRepository).save(accountCaptor.capture());
    assertThat(accountCaptor.getValue().getEmail()).isEqualTo("hello@world");
    assertThat(accountCaptor.getValue().getUsername()).isEqualTo("username");
    assertThat(accountCaptor.getValue().getPassword()).isEqualTo("password");
  }
  
  @Test
  void testRegisterNewUserAlreadyExists() {
    RegisterDto dto = new RegisterDto();
    char[] password = {'p', 'a', 's', 's'};
    dto.setDateOfBirth(LocalDate.of(2012, 11, 13));
    dto.setEmail("hello@world");
    dto.setUsername("username");
    dto.setPassword(password);
    dto.setTos_agree(true);
    
    Mockito.when(accountRepository.findByEmail("hello@world"))
           .thenReturn(new Account());
    
    assertThatThrownBy(() -> underTest.registerNewAccount(dto))
        .isInstanceOf(UsernameAlreadyExistsException.class)
        .hasMessageContaining("Error: email is already associated with an account.");
  }
}
