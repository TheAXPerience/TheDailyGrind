package com.alvinxu.TheDailyGrind.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.DiaryEntry;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class TestDiaryEntryRepository {
  @Autowired private AccountRepository accountRepository;
  @Autowired private DiaryEntryRepository underTest;
  
  private Account user1;
  private Account user2;
  private DiaryEntry entry1;
  private DiaryEntry entry2;
  private DiaryEntry entry3;
  private DiaryEntry entry4;
  
  @BeforeAll
  void setup() {
    user1 = new Account();
    user1.setUsername("username1");
    user1.setPassword("password1");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 4, 20, 15, 36));
    user1.setAuthority("USER");
    accountRepository.save(user1);
    
    user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2014, 6, 9, 13, 56));
    user2.setAuthority("USER");
    accountRepository.save(user2);
    
    entry1 = new DiaryEntry();
    entry1.setDiaryOwner(user1);
    entry1.setTitle("title1");
    entry1.setEntry("description");
    entry1.setDateOfEntry(LocalDateTime.of(2023, 8, 13, 5, 7));
    underTest.save(entry1);
    
    entry2 = new DiaryEntry();
    entry2.setDiaryOwner(user1);
    entry2.setTitle("title2");
    entry2.setEntry("description");
    entry2.setDateOfEntry(LocalDateTime.of(2023, 8, 19, 5, 7));
    underTest.save(entry2);
    
    entry3 = new DiaryEntry();
    entry3.setDiaryOwner(user1);
    entry3.setTitle("title3");
    entry3.setEntry("description");
    entry3.setDateOfEntry(LocalDateTime.of(2023, 9, 10, 5, 7));
    underTest.save(entry3);
    
    entry4 = new DiaryEntry();
    entry4.setDiaryOwner(user2);
    entry4.setTitle("title4");
    entry4.setEntry("description");
    entry4.setDateOfEntry(LocalDateTime.of(2023, 8, 12, 5, 7));
    underTest.save(entry4);
  }
  
  @AfterAll
  void teardown() {
    accountRepository.deleteAll();
    underTest.deleteAll();
  }
  
  @Test
  void testFindAllByAccountId() {
    // with
    Pageable pageable = PageRequest.of(0, 5);
    
    // when
    Page<DiaryEntry> resultsPrivate = underTest.findAllByAccountId(user1.getId(), pageable);
    
    // then
    List<DiaryEntry> results = resultsPrivate.getContent();
    assertThat(results.size()).isEqualTo(3);
    // not sure how to check if certain events exist
    for (DiaryEntry e : results) {
      assertThat(e.getId()).isIn(List.of(entry1.getId(), entry2.getId(), entry3.getId()));
      assertThat(e.getId()).isNotEqualTo(entry4.getId());
    }
  }
  
  @Test
  void testFindAllOfAccountBeforeDate() {
    // with
    Pageable pageable = PageRequest.of(0, 5);
    
    // when
    Page<DiaryEntry> resultsPrivate =
        underTest.findAllOfAccountBeforeDate(
            user1.getId(), LocalDateTime.of(2023, 8, 20, 0, 0), pageable
        );
    
    // then
    List<DiaryEntry> results = resultsPrivate.getContent();
    assertThat(results.size()).isEqualTo(2);
    // not sure how to check if certain events exist
    for (DiaryEntry e : results) {
      assertThat(e.getId()).isIn(List.of(entry2.getId(), entry1.getId()));
      assertThat(e.getId()).isNotEqualTo(entry3.getId());
      assertThat(e.getId()).isNotEqualTo(entry4.getId());
    }
  }
  
  @Test
  void testFindAllOfAccountBetweenDates() {
    // with
    Pageable pageable = PageRequest.of(0, 5);
    
    // when
    Page<DiaryEntry> resultsAll =
        underTest.findAllOfAccountBetweenDates(
            user1.getId(),
            LocalDateTime.of(2023, 8, 10, 0, 0),
            LocalDateTime.of(2023, 8, 15, 0, 0),
            pageable
        );
    
    // then
    List<DiaryEntry> results = resultsAll.getContent();
    assertThat(results.size()).isEqualTo(1);
    assertThat(results.get(0).getId()).isEqualTo(entry1.getId());
  }
}
