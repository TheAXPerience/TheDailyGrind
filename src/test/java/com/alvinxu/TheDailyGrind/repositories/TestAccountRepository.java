package com.alvinxu.TheDailyGrind.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alvinxu.TheDailyGrind.models.Account;

@DataJpaTest
public class TestAccountRepository {
  @Autowired
  private AccountRepository underTest;
  
  @AfterEach
  void tearDown() {
    underTest.deleteAll();
  }
  
  @Test
  void testFindByEmail() {
    // given
    LocalDateTime date = LocalDateTime.of(2012, 12, 21, 8, 16);
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail("hello@world");
    user.setDateOfBirth(date);
    user.setAuthority("USER");
    underTest.save(user);
    
    // when
    Account found = underTest.findByEmail("hello@world");
    
    // then
    assertThat(found).isEqualTo(user);
  }
  
  @Test
  void testFindByEmailNoResult() {
    // given... nothing
    
    // when
    Account found = underTest.findByEmail("hello@world");
    assertThat(found).isNull();
  }
  
  @Test
  void testFindSimilarToUsername() {
    // given
    Account user1 = new Account();
    user1.setUsername("username");
    user1.setPassword("password");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 7, 16, 5, 40));
    user1.setAuthority("USER1");
    underTest.save(user1);
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2013, 4, 20, 6, 9));
    user2.setAuthority("USER2");
    underTest.save(user2);
    
    Account user3 = new Account();
    user3.setUsername("user");
    user3.setPassword("password3");
    user3.setEmail("hello@bonk");
    user3.setDateOfBirth(LocalDateTime.of(2017, 7, 7, 8, 49));
    user3.setAuthority("USER3");
    underTest.save(user3);
    
    Account user4 = new Account();
    user4.setUsername("florida");
    user4.setPassword("password4");
    user4.setEmail("hello@clock");
    user4.setDateOfBirth(LocalDateTime.of(2015, 1, 11, 12, 13));
    user4.setAuthority("USER4");
    underTest.save(user4);
    
    Account user5 = new Account();
    user5.setUsername("sparkleuser");
    user5.setPassword("password5");
    user5.setEmail("hello@dork");
    user5.setDateOfBirth(LocalDateTime.of(2014, 8, 21, 16, 27));
    user5.setAuthority("USER5");
    underTest.save(user5);
    
    // when
    Pageable pageable = PageRequest.of(0, 5);
    Page<Account> result = underTest.findSimilarToUsername("user", pageable);
    
    // then
    List<Account> resultList = result.getContent();
    
    assertThat(resultList.contains(user1)).isTrue();
    assertThat(resultList.contains(user2)).isTrue();
    assertThat(resultList.contains(user3)).isTrue();
    assertThat(resultList.contains(user4)).isFalse();
    assertThat(resultList.contains(user5)).isTrue();
  }
}
