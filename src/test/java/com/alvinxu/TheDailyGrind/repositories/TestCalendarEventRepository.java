package com.alvinxu.TheDailyGrind.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.CalendarEvent;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class TestCalendarEventRepository {
  @Autowired private AccountRepository accountRepository;
  @Autowired private CalendarEventRepository underTest;
  
  private Account user1;
  private Account user2;
  private CalendarEvent event1;
  private CalendarEvent event2;
  private CalendarEvent event3;
  private CalendarEvent event4;
  
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
    
    event1 = new CalendarEvent();
    event1.setEventOrganizer(user1);
    event1.setTitle("title1");
    event1.setDescription("description");
    event1.setDateOfEvent(LocalDateTime.of(2023, 8, 13, 5, 7));
    event1.setPublic(true);
    event1.setComplete(false);
    underTest.save(event1);
    
    event2 = new CalendarEvent();
    event2.setEventOrganizer(user1);
    event2.setTitle("title2");
    event2.setDescription("description");
    event2.setDateOfEvent(LocalDateTime.of(2023, 8, 19, 5, 7));
    event2.setPublic(true);
    event2.setComplete(false);
    underTest.save(event2);
    
    event3 = new CalendarEvent();
    event3.setEventOrganizer(user1);
    event3.setTitle("title3");
    event3.setDescription("description");
    event3.setDateOfEvent(LocalDateTime.of(2023, 9, 10, 5, 7));
    event3.setPublic(false);
    event3.setComplete(false);
    underTest.save(event3);
    
    event4 = new CalendarEvent();
    event4.setEventOrganizer(user2);
    event4.setTitle("title4");
    event4.setDescription("description");
    event4.setDateOfEvent(LocalDateTime.of(2023, 8, 12, 5, 7));
    event4.setPublic(true);
    event4.setComplete(false);
    underTest.save(event4);
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
    Page<CalendarEvent> resultsPrivate = underTest.findAllByAccountId(user1.getId(), true, pageable);
    Page<CalendarEvent> resultsPublic = underTest.findAllByAccountId(user1.getId(), false, pageable);
    
    // then
    List<CalendarEvent> results = resultsPrivate.getContent();
    assertThat(results.size()).isEqualTo(3);
    // not sure how to check if certain events exist
    for (CalendarEvent e : results) {
      assertThat(e.getId()).isIn(List.of(event1.getId(), event2.getId(), event3.getId()));
      assertThat(e.getId()).isNotEqualTo(event4.getId());
    }
    
    results = resultsPublic.getContent();
    assertThat(results.size()).isEqualTo(2);
    for (CalendarEvent e : results) {
      assertThat(e.getId()).isIn(List.of(event1.getId(), event2.getId()));
      assertThat(e.getId()).isNotEqualTo(event3.getId());
      assertThat(e.getId()).isNotEqualTo(event4.getId());
    }
  }
  
  @Test
  void testFindAllOfAccountAfterDate() {
    // with
    Pageable pageable = PageRequest.of(0, 5);
    
    // when
    Page<CalendarEvent> resultsPrivate =
        underTest.findAllOfAccountAfterDate(
            user1.getId(), true, LocalDateTime.of(2023, 8, 15, 0, 0), pageable
        );
    Page<CalendarEvent> resultsPublic =
        underTest.findAllOfAccountAfterDate(
            user1.getId(), false, LocalDateTime.of(2023, 8, 15, 0, 0), pageable
        );
    
    // then
    List<CalendarEvent> results = resultsPrivate.getContent();
    assertThat(results.size()).isEqualTo(2);
    // not sure how to check if certain events exist
    for (CalendarEvent e : results) {
      assertThat(e.getId()).isIn(List.of(event2.getId(), event3.getId()));
      assertThat(e.getId()).isNotEqualTo(event1.getId());
      assertThat(e.getId()).isNotEqualTo(event4.getId());
    }
    
    results = resultsPublic.getContent();
    assertThat(results.size()).isEqualTo(1);
    assertThat(results.get(0).getId()).isEqualTo(event2.getId());
  }
  
  @Test
  void testFindAllOfAccountBetweenDates() {
    // with
    Pageable pageable = PageRequest.of(0, 5);
    
    // when
    Page<CalendarEvent> resultsAll =
        underTest.findAllOfAccountBetweenDates(
            user1.getId(),
            true,
            LocalDateTime.of(2023, 8, 10, 0, 0),
            LocalDateTime.of(2023, 8, 15, 0, 0),
            pageable
        );

    // then
    List<CalendarEvent> results = resultsAll.getContent();
    assertThat(results.size()).isEqualTo(1);
    assertThat(results.get(0).getId()).isEqualTo(event1.getId());
  }
}
