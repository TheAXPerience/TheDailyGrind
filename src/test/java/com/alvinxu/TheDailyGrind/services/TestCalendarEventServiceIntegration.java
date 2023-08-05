package com.alvinxu.TheDailyGrind.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.alvinxu.TheDailyGrind.dto.CalendarEventDto;
import com.alvinxu.TheDailyGrind.exceptions.EventEntryNotFoundException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.CalendarEvent;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;
import com.alvinxu.TheDailyGrind.repositories.CalendarEventRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class TestCalendarEventServiceIntegration {
  @Autowired private CalendarEventRepository repository;
  @Autowired private AccountRepository accRepository;
  private AccountService accounts;
  private CalendarEventService underTest;
  
  @BeforeAll
  void setup() {
    // autowiring doesn't work???
    accounts = new AccountService(accRepository, new BCryptPasswordEncoder());
    underTest = new CalendarEventService(accounts, repository);
  }
  
  @AfterEach
  void teardown() {
    repository.deleteAll();
    accRepository.deleteAll();
  }
  
  @Test
  void testGetById() {
    Account user1 = new Account();
    user1.setUsername("username1");
    user1.setPassword("password1");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 4, 20, 15, 36));
    user1.setAuthority("USER");
    accRepository.save(user1);
    
    CalendarEvent event1 = new CalendarEvent();
    event1.setEventOrganizer(user1);
    event1.setTitle("title1");
    event1.setDescription("description");
    event1.setDateOfEvent(LocalDateTime.of(2023, 8, 13, 5, 7));
    event1.setPublic(true);
    event1.setComplete(false);
    repository.save(event1);
    
    CalendarEvent expected = underTest.getById(event1.getId());
    
    assertThat(expected).isEqualTo(event1);
  }
  
  @Test
  void testGetByIdDoesNotExist() {
    assertThatThrownBy(() -> underTest.getById(5L))
        .isInstanceOf(EventEntryNotFoundException.class)
        .hasMessageContaining("Error: event does not exist.");
  }
  
  @Test
  void testSaveNewCalendarEvent() {
    CalendarEventDto dto = new CalendarEventDto();
    dto.setTitle("title");
    dto.setDescription("description");
    dto.setDateOfEvent(LocalDateTime.of(2023, 8, 14, 5, 30));
    dto.setPublicToggle(true);
    dto.setCompleteToggle(false);
    
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    underTest.saveNewCalendarEvent(email, dto);
    
    List<CalendarEvent> events = repository.findAll();
    
    assertThat(events.size()).isEqualTo(1);
    CalendarEvent event = events.get(0);
    
    assertThat(event.getEventOrganizer()).isEqualTo(user);
    assertThat(event.getTitle()).isEqualTo(dto.getTitle());
    assertThat(event.getDescription()).isEqualTo(dto.getDescription());
    assertThat(event.getDateOfEvent()).isEqualTo(dto.getDateOfEvent());
    assertThat(event.isPublic()).isEqualTo(dto.getPublicToggle());
    assertThat(event.isComplete()).isEqualTo(dto.getCompleteToggle());
  }
  
  @Test
  void testSaveNewCalendarEventBadUser() {
    CalendarEventDto dto = new CalendarEventDto();
    dto.setTitle("title");
    dto.setDescription("description");
    dto.setDateOfEvent(LocalDateTime.of(2023, 8, 14, 5, 30));
    dto.setPublicToggle(true);
    dto.setCompleteToggle(false);
    
    String email = "hello@world";
    
    assertThatThrownBy(() -> underTest.saveNewCalendarEvent(email, dto))
    .isInstanceOf(UsernameNotFoundException.class)
    .hasMessageContaining("Error: could not verify user.");
  }
  
  @Test
  void testUpdateCalendarEvent() throws EventEntryNotFoundException, IllegalAccessException {
    CalendarEventDto dto = new CalendarEventDto();
    dto.setTitle("title");
    dto.setDescription("description");
    dto.setDateOfEvent(LocalDateTime.of(2023, 8, 14, 5, 30));
    dto.setPublicToggle(true);
    dto.setCompleteToggle(false);
    
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    CalendarEvent event = new CalendarEvent();
    event.setComplete(true);
    event.setDateOfEvent(LocalDateTime.of(2023, 8, 15, 4, 29));
    event.setDescription("old description");
    event.setEventOrganizer(user);
    event.setPublic(false);
    event.setTitle("old title");
    repository.save(event);
    
    underTest.updateCalendarEvent(event.getId(), email, dto);
    
    Optional<CalendarEvent> expected = repository.findById(event.getId());
    assertThat(expected.isEmpty()).isFalse();
    
    assertThat(expected.get().getId()).isEqualTo(event.getId());
    assertThat(expected.get().getTitle()).isEqualTo(dto.getTitle());
    assertThat(expected.get().getDescription()).isEqualTo(dto.getDescription());
    assertThat(expected.get().getDateOfEvent()).isEqualTo(dto.getDateOfEvent());
    assertThat(expected.get().isPublic()).isEqualTo(dto.getPublicToggle());
    assertThat(expected.get().isComplete()).isEqualTo(dto.getCompleteToggle());
  }
  
  @Test
  void testUpdateCalendarEventDoesNotExist() {
    CalendarEventDto dto = new CalendarEventDto();
    dto.setTitle("title");
    dto.setDescription("description");
    dto.setDateOfEvent(LocalDateTime.of(2023, 8, 14, 5, 30));
    dto.setPublicToggle(true);
    dto.setCompleteToggle(false);
    
    assertThatThrownBy(() -> underTest.updateCalendarEvent(5L, "hello@world", dto))
        .isInstanceOf(EventEntryNotFoundException.class)
        .hasMessageContaining("Error: event does not exist.");
  }
  
  @Test
  void testUpdateCalendarEventBadUser() {
    CalendarEventDto dto = new CalendarEventDto();
    dto.setTitle("title");
    dto.setDescription("description");
    dto.setDateOfEvent(LocalDateTime.of(2023, 8, 14, 5, 30));
    dto.setPublicToggle(true);
    dto.setCompleteToggle(false);
    
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    CalendarEvent event = new CalendarEvent();
    event.setComplete(true);
    event.setDateOfEvent(LocalDateTime.of(2023, 8, 15, 4, 29));
    event.setDescription("old description");
    event.setEventOrganizer(user);
    event.setPublic(false);
    event.setTitle("old title");
    repository.save(event);
    
    assertThatThrownBy(() -> underTest.updateCalendarEvent(event.getId(), "hello@jank", dto))
        .isInstanceOf(IllegalAccessException.class)
        .hasMessageContaining("Error: user does not have permission to edit event.");
  }
  
  @Test
  void testDeleteCalendarEvent() throws EventEntryNotFoundException, IllegalAccessException {
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    CalendarEvent event = new CalendarEvent();
    event.setComplete(true);
    event.setDateOfEvent(LocalDateTime.of(2023, 8, 15, 4, 29));
    event.setDescription("old description");
    event.setEventOrganizer(user);
    event.setPublic(false);
    event.setTitle("old title");
    repository.save(event);
    
    underTest.deleteCalendarEvent(event.getId(), email);
    
    Optional<CalendarEvent> expected = repository.findById(event.getId());
    assertThat(expected.isEmpty()).isTrue();
  }
  
  @Test
  void testDeleteCalendarEventDoesNotExist() {
    assertThatThrownBy(() -> underTest.deleteCalendarEvent(5L, "hello@world"))
        .isInstanceOf(EventEntryNotFoundException.class)
        .hasMessageContaining("Error: event does not exist.");
  }
  
  @Test
  void testDeleteCalendarEventBadUser() {
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    CalendarEvent event = new CalendarEvent();
    event.setComplete(true);
    event.setDateOfEvent(LocalDateTime.of(2023, 8, 15, 4, 29));
    event.setDescription("old description");
    event.setEventOrganizer(user);
    event.setPublic(false);
    event.setTitle("old title");
    repository.save(event);
    
    assertThatThrownBy(() -> underTest.deleteCalendarEvent(event.getId(), "hello@jank"))
        .isInstanceOf(IllegalAccessException.class)
        .hasMessageContaining("Error: user does not have permission to delete event.");
  }
  
  @Test
  void testGetAllEventsOfAccount() {
    Account user1 = new Account();
    user1.setUsername("username1");
    user1.setPassword("password1");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 4, 20, 15, 36));
    user1.setAuthority("USER");
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2014, 6, 9, 13, 56));
    user2.setAuthority("USER");
    
    CalendarEvent event1 = new CalendarEvent();
    event1.setEventOrganizer(user1);
    event1.setTitle("title1");
    event1.setDescription("description");
    event1.setDateOfEvent(LocalDateTime.of(2023, 8, 13, 5, 7));
    event1.setPublic(true);
    event1.setComplete(false);
    
    CalendarEvent event2 = new CalendarEvent();
    event2.setEventOrganizer(user1);
    event2.setTitle("title2");
    event2.setDescription("description");
    event2.setDateOfEvent(LocalDateTime.of(2023, 8, 19, 5, 7));
    event2.setPublic(true);
    event2.setComplete(false);
    
    CalendarEvent event3 = new CalendarEvent();
    event3.setEventOrganizer(user1);
    event3.setTitle("title3");
    event3.setDescription("description");
    event3.setDateOfEvent(LocalDateTime.of(2023, 9, 10, 5, 7));
    event3.setPublic(false);
    event3.setComplete(false);
    
    CalendarEvent event4 = new CalendarEvent();
    event4.setEventOrganizer(user2);
    event4.setTitle("title4");
    event4.setDescription("description");
    event4.setDateOfEvent(LocalDateTime.of(2023, 8, 12, 5, 7));
    event4.setPublic(true);
    event4.setComplete(false);
    
    accRepository.save(user1);
    accRepository.save(user2);
    repository.save(event1);
    repository.save(event2);
    repository.save(event3);
    repository.save(event4);
    
    Page<CalendarEvent> expected = underTest.getAllEventsOfAccount(user1.getId(), true, 0, 5);
    
    List<CalendarEvent> content = expected.getContent();
    assertThat(content.size()).isEqualTo(3);
    assertThat(content.contains(event1)).isTrue();
    assertThat(content.contains(event2)).isTrue();
    assertThat(content.contains(event3)).isTrue();
    assertThat(content.contains(event4)).isFalse();
  }
  
  @Test
  void testGetAllEventsAfterDate() {
    Account user1 = new Account();
    user1.setUsername("username1");
    user1.setPassword("password1");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 4, 20, 15, 36));
    user1.setAuthority("USER");
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2014, 6, 9, 13, 56));
    user2.setAuthority("USER");
    
    CalendarEvent event1 = new CalendarEvent();
    event1.setEventOrganizer(user1);
    event1.setTitle("title1");
    event1.setDescription("description");
    event1.setDateOfEvent(LocalDateTime.of(2023, 8, 13, 5, 7));
    event1.setPublic(true);
    event1.setComplete(false);
    
    CalendarEvent event2 = new CalendarEvent();
    event2.setEventOrganizer(user1);
    event2.setTitle("title2");
    event2.setDescription("description");
    event2.setDateOfEvent(LocalDateTime.of(2023, 8, 19, 5, 7));
    event2.setPublic(true);
    event2.setComplete(false);
    
    CalendarEvent event3 = new CalendarEvent();
    event3.setEventOrganizer(user1);
    event3.setTitle("title3");
    event3.setDescription("description");
    event3.setDateOfEvent(LocalDateTime.of(2023, 9, 10, 5, 7));
    event3.setPublic(false);
    event3.setComplete(false);
    
    CalendarEvent event4 = new CalendarEvent();
    event4.setEventOrganizer(user2);
    event4.setTitle("title4");
    event4.setDescription("description");
    event4.setDateOfEvent(LocalDateTime.of(2023, 8, 12, 5, 7));
    event4.setPublic(true);
    event4.setComplete(false);
    
    accRepository.save(user1);
    accRepository.save(user2);
    repository.save(event1);
    repository.save(event2);
    repository.save(event3);
    repository.save(event4);
    
    LocalDateTime date = LocalDateTime.of(2023, 8, 15, 0, 0);
    
    Page<CalendarEvent> expected = underTest.getAllEventsAfterDate(user1.getId(), true, date, 0, 5);
    
    List<CalendarEvent> content = expected.getContent();
    assertThat(content.size()).isEqualTo(2);
    assertThat(content.contains(event1)).isFalse();
    assertThat(content.contains(event2)).isTrue();
    assertThat(content.contains(event3)).isTrue();
    assertThat(content.contains(event4)).isFalse();
  }
  
  @Test
  void testGetAllEventsBetweenDates() {
    Account user1 = new Account();
    user1.setUsername("username1");
    user1.setPassword("password1");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 4, 20, 15, 36));
    user1.setAuthority("USER");
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2014, 6, 9, 13, 56));
    user2.setAuthority("USER");
    
    CalendarEvent event1 = new CalendarEvent();
    event1.setEventOrganizer(user1);
    event1.setTitle("title1");
    event1.setDescription("description");
    event1.setDateOfEvent(LocalDateTime.of(2023, 8, 13, 5, 7));
    event1.setPublic(true);
    event1.setComplete(false);
    
    CalendarEvent event2 = new CalendarEvent();
    event2.setEventOrganizer(user1);
    event2.setTitle("title2");
    event2.setDescription("description");
    event2.setDateOfEvent(LocalDateTime.of(2023, 8, 19, 5, 7));
    event2.setPublic(true);
    event2.setComplete(false);
    
    CalendarEvent event3 = new CalendarEvent();
    event3.setEventOrganizer(user1);
    event3.setTitle("title3");
    event3.setDescription("description");
    event3.setDateOfEvent(LocalDateTime.of(2023, 9, 10, 5, 7));
    event3.setPublic(false);
    event3.setComplete(false);
    
    CalendarEvent event4 = new CalendarEvent();
    event4.setEventOrganizer(user2);
    event4.setTitle("title4");
    event4.setDescription("description");
    event4.setDateOfEvent(LocalDateTime.of(2023, 8, 12, 5, 7));
    event4.setPublic(true);
    event4.setComplete(false);
    
    accRepository.save(user1);
    accRepository.save(user2);
    repository.save(event1);
    repository.save(event2);
    repository.save(event3);
    repository.save(event4);
    
    LocalDateTime date = LocalDateTime.of(2023, 8, 15, 0, 0);
    LocalDateTime date2 = LocalDateTime.of(2023, 8, 20, 0, 0);
    
    Page<CalendarEvent> expected = underTest.getAllEventsBetweenDates(user1.getId(), true, date, date2, 0, 5);
    
    List<CalendarEvent> content = expected.getContent();
    assertThat(content.size()).isEqualTo(1);
    assertThat(content.contains(event1)).isFalse();
    assertThat(content.contains(event2)).isTrue();
    assertThat(content.contains(event3)).isFalse();
    assertThat(content.contains(event4)).isFalse();
  }
}
