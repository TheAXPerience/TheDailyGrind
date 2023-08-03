package com.alvinxu.TheDailyGrind.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/*
 * A series of tests to confirm the validity of the getters and setters of Account
 */
public class TestAccount {
  @Test
  void testId() {
    // with
    Account user = new Account();
    
    // when
    user.setId(3L);
    
    // then
    assertThat(user.getId()).isEqualTo(3L);
  }
  
  @Test
  void testEmail() {
 // with
    Account user = new Account();
    String email = "hello@world";
    
    // when
    user.setEmail(email);
    
    // then
    assertThat(user.getEmail()).isEqualTo(email);
  }
  
  @Test
  void testUsername() {
    // with
    Account user = new Account();
    String username = "username";
    
    // when
    user.setUsername(username);
    
    // then
    assertThat(user.getUsername()).isEqualTo(username);
  }
  
  @Test
  void testPassword() {
    // with
    Account user = new Account();
    String password = "password";
    
    // when
    user.setPassword(password);
    
    // then
    assertThat(user.getPassword()).isEqualTo(password);
  }
  
  @Test
  void testDateOfBirth() {
    // with
    Account user = new Account();
    LocalDateTime dateOfBirth = LocalDateTime.of(2000, 1, 1, 0, 0);
    
    // when
    user.setDateOfBirth(dateOfBirth);
    
    // then
    assertThat(user.getDateOfBirth()).isEqualTo(dateOfBirth);
  }
  
  @Test
  void testAuthority() {
    // with
    Account user = new Account();
    String authority = "USER";
    
    // when
    user.setAuthority(authority);
    
    // then
    assertThat(user.getAuthority()).isEqualTo(authority);
  }
  
  @Test
  void testEquality() {
    // when
    Account user = new Account(
        2L,
        "katie@zoo.com",
        "Katie Zu",
        "danganronpa",
        LocalDateTime.of(2005, 6, 9, 4, 20),
        "ADMIN"
    );
    
    Account user2 = new Account(
        2L,
        "katie@zoo.com",
        "Katie Zu",
        "danganronpa",
        LocalDateTime.of(2005, 6, 9, 4, 20),
        "ADMIN"
    );
    
    Account user3 = new Account(
        3L,
        "hello@world",
        "username",
        "password",
        LocalDateTime.of(2000, 1, 1, 0, 0),
        "USER"
    );
    
    Account user4 = new Account(
        4L,
        "hello@world",
        "username",
        "password",
        LocalDateTime.of(2000, 1, 1, 0, 0),
        "USER"
    );
    
    Account user5 = new Account(
        3L,
        "hell0@world",
        "username",
        "password",
        LocalDateTime.of(2000, 1, 1, 0, 0),
        "USER"
    );
    
    assertThat(user.equals(user2)).isTrue();
    assertThat(user2.equals(user)).isTrue();
    assertThat(user.hashCode()).isEqualTo(user2.hashCode());
    
    assertThat(user.equals(user3)).isFalse();
    assertThat(user.equals(user4)).isFalse();
    assertThat(user.equals(user5)).isFalse();
    assertThat(user4.equals(user3)).isFalse();
    assertThat(user5.equals(user3)).isFalse();
    assertThat(user.hashCode()).isNotEqualTo(user3.hashCode());
    assertThat(user.hashCode()).isNotEqualTo(user4.hashCode());
    assertThat(user4.hashCode()).isNotEqualTo(user3.hashCode());
  }
}
