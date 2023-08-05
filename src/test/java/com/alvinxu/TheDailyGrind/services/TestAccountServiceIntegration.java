package com.alvinxu.TheDailyGrind.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.CharBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alvinxu.TheDailyGrind.dto.RegisterDto;
import com.alvinxu.TheDailyGrind.exceptions.UsernameAlreadyExistsException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class TestAccountServiceIntegration {
  @Autowired private AccountRepository accountRepository;
  private PasswordEncoder passwordEncoder;
  private AccountService underTest;
  
  @BeforeAll
  void firstSetup() {
    passwordEncoder = new BCryptPasswordEncoder();
    underTest = new AccountService(accountRepository, passwordEncoder);
  }
  
  @AfterEach
  void teardown() {
    accountRepository.deleteAll();
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
    accountRepository.save(user);
    
    Account expected = underTest.getAccountById(user.getId());
    
    assertThat(expected).isEqualTo(user);
  }
  
  @Test
  void testGetAccountByIdInvalidUser() {
    Account expected = underTest.getAccountById(1L);
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
    accountRepository.save(user);
    
    Account expected = underTest.getAccountByEmail(email);
    
    assertThat(expected).isEqualTo(user);
  }
  
  @Test
  void testGetAccountByEmailInvalidEmail() {
    String email = "hello@world";
    
    Account expected = underTest.getAccountByEmail(email);

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
    accountRepository.save(user1);
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2013, 4, 20, 6, 9));
    user2.setAuthority("USER2");
    accountRepository.save(user2);
    
    Account user3 = new Account();
    user3.setUsername("user");
    user3.setPassword("password3");
    user3.setEmail("hello@bonk");
    user3.setDateOfBirth(LocalDateTime.of(2017, 7, 7, 8, 49));
    user3.setAuthority("USER3");
    accountRepository.save(user3);
    
    Page<Account> expected = underTest.getAccountsLikeUsername("user", 0, 5);
    
    assertThat(expected.getContent().size()).isEqualTo(3);
    List<Account> content = expected.getContent();
    assertThat(content.contains(user1)).isTrue();
    assertThat(content.contains(user2)).isTrue();
    assertThat(content.contains(user3)).isTrue();
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
    
    underTest.registerNewAccount(dto);
    
    Account expected = underTest.getAccountByEmail(dto.getEmail());
    assertThat(expected.getEmail()).isEqualTo(dto.getEmail());
    assertThat(expected.getUsername()).isEqualTo(dto.getUsername());
    assertThat(expected.getDateOfBirth()).isEqualTo(dto.getDateOfBirth().atStartOfDay());
    // assertThat(expected.getPassword()).isEqualTo(passwordEncoder.encode(CharBuffer.wrap(password)));
    assertThat(passwordEncoder.matches(CharBuffer.wrap(password), expected.getPassword())).isTrue();
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
    
    Account user1 = new Account();
    user1.setUsername("username");
    user1.setPassword("password");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 7, 16, 5, 40));
    user1.setAuthority("USER1");
    accountRepository.save(user1);
    
    assertThatThrownBy(() -> underTest.registerNewAccount(dto))
        .isInstanceOf(UsernameAlreadyExistsException.class)
        .hasMessageContaining("Error: email is already associated with an account.");
  }
}
