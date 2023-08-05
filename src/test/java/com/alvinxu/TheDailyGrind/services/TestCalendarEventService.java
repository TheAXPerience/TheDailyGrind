package com.alvinxu.TheDailyGrind.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alvinxu.TheDailyGrind.dto.CalendarEventDto;
import com.alvinxu.TheDailyGrind.exceptions.EventEntryNotFoundException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.CalendarEvent;
import com.alvinxu.TheDailyGrind.repositories.CalendarEventRepository;

@ExtendWith(MockitoExtension.class)
public class TestCalendarEventService {
  @Mock private CalendarEventRepository repository;
  @Mock private AccountService accounts;
  private CalendarEventService underTest;
  
  @BeforeEach
  void setup() {
    underTest = new CalendarEventService(accounts, repository);
  }
  
  @Test
  void testGetById() {
    CalendarEvent event = new CalendarEvent();
    event.setId(5L);
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(event));
    
    CalendarEvent expected = underTest.getById(5L);
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(repository).findById(idCaptor.capture());
    assertThat(expected).isEqualTo(event);
  }
  
  @Test
  void testGetByIdDoesNotExist() {
    Mockito.when(repository.findById(5L)).thenReturn(Optional.empty());
    
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
    
    Mockito.when(accounts.getAccountByEmail(email)).thenReturn(user);
    
    underTest.saveNewCalendarEvent(email, dto);
    
    ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<CalendarEvent> eventCaptor = ArgumentCaptor.forClass(CalendarEvent.class);
    
    verify(accounts).getAccountByEmail(emailCaptor.capture());
    verify(repository).save(eventCaptor.capture());
    assertThat(emailCaptor.getValue()).isEqualTo(email);
    assertThat(eventCaptor.getValue().getTitle()).isEqualTo(dto.getTitle());
    assertThat(eventCaptor.getValue().getDescription()).isEqualTo(dto.getDescription());
    assertThat(eventCaptor.getValue().getDateOfEvent()).isEqualTo(dto.getDateOfEvent());
    assertThat(eventCaptor.getValue().isPublic()).isEqualTo(dto.getPublicToggle());
    assertThat(eventCaptor.getValue().isComplete()).isEqualTo(dto.getCompleteToggle());
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
    
    Mockito.when(accounts.getAccountByEmail(email)).thenReturn(null);
    
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
    
    CalendarEvent event = new CalendarEvent();
    event.setComplete(true);
    event.setDateOfEvent(LocalDateTime.of(2023, 8, 15, 4, 29));
    event.setDescription("old description");
    event.setEventOrganizer(user);
    event.setPublic(false);
    event.setTitle("old title");
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(event));
    
    underTest.updateCalendarEvent(5L, email, dto);
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<CalendarEvent> eventCaptor = ArgumentCaptor.forClass(CalendarEvent.class);
    
    verify(repository).findById(idCaptor.capture());
    verify(repository).save(eventCaptor.capture());
    
    assertThat(idCaptor.getValue()).isEqualTo(5L);
    assertThat(eventCaptor.getValue().getTitle()).isEqualTo(dto.getTitle());
    assertThat(eventCaptor.getValue().getDescription()).isEqualTo(dto.getDescription());
    assertThat(eventCaptor.getValue().getDateOfEvent()).isEqualTo(dto.getDateOfEvent());
    assertThat(eventCaptor.getValue().isPublic()).isEqualTo(dto.getPublicToggle());
    assertThat(eventCaptor.getValue().isComplete()).isEqualTo(dto.getCompleteToggle());
  }
  
  @Test
  void testUpdateCalendarEventDoesNotExist() {
    CalendarEventDto dto = new CalendarEventDto();
    dto.setTitle("title");
    dto.setDescription("description");
    dto.setDateOfEvent(LocalDateTime.of(2023, 8, 14, 5, 30));
    dto.setPublicToggle(true);
    dto.setCompleteToggle(false);
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.empty());
    
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
    
    CalendarEvent event = new CalendarEvent();
    event.setComplete(true);
    event.setDateOfEvent(LocalDateTime.of(2023, 8, 15, 4, 29));
    event.setDescription("old description");
    event.setEventOrganizer(user);
    event.setPublic(false);
    event.setTitle("old title");
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(event));
    
    assertThatThrownBy(() -> underTest.updateCalendarEvent(5L, "hello@jank", dto))
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
    
    CalendarEvent event = new CalendarEvent();
    event.setComplete(true);
    event.setDateOfEvent(LocalDateTime.of(2023, 8, 15, 4, 29));
    event.setDescription("old description");
    event.setEventOrganizer(user);
    event.setPublic(false);
    event.setTitle("old title");
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(event));
    
    underTest.deleteCalendarEvent(5L, email);
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<CalendarEvent> eventCaptor = ArgumentCaptor.forClass(CalendarEvent.class);
    
    verify(repository).findById(idCaptor.capture());
    verify(repository).delete(eventCaptor.capture());
    
    assertThat(idCaptor.getValue()).isEqualTo(5L);
    assertThat(eventCaptor.getValue()).isEqualTo(event);
  }
  
  @Test
  void testDeleteCalendarEventDoesNotExist() {
    Mockito.when(repository.findById(5L)).thenReturn(Optional.empty());
    
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
    
    CalendarEvent event = new CalendarEvent();
    event.setComplete(true);
    event.setDateOfEvent(LocalDateTime.of(2023, 8, 15, 4, 29));
    event.setDescription("old description");
    event.setEventOrganizer(user);
    event.setPublic(false);
    event.setTitle("old title");
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(event));
    
    assertThatThrownBy(() -> underTest.deleteCalendarEvent(5L, "hello@jank"))
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
    
    Pageable pageable = PageRequest.of(0, 5);
    
    Mockito.when(repository.findAllByAccountId(5L, true, pageable)).thenReturn(
        new PageImpl<CalendarEvent>(List.of(event1, event2, event3), pageable, 3)
    );
    
    Page<CalendarEvent> expected = underTest.getAllEventsOfAccount(5L, true, 0, 5);
    
    ArgumentCaptor<Long> paramCapture = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Boolean> boolCapture = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<Pageable> pageCapture = ArgumentCaptor.forClass(Pageable.class);
    verify(repository).findAllByAccountId(paramCapture.capture(), boolCapture.capture(), pageCapture.capture());
    assertThat(expected.getContent().size()).isEqualTo(3);
    assertThat(paramCapture.getValue()).isEqualTo(5L);
    assertThat(boolCapture.getValue()).isTrue();
    assertThat(pageCapture.getValue()).isEqualTo(pageable);
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
    
    Pageable pageable = PageRequest.of(0, 5);
    LocalDateTime date = LocalDateTime.of(2023, 8, 15, 0, 0);
    
    Mockito.when(repository.findAllOfAccountAfterDate(5L, true, date, pageable))
    .thenReturn(
        new PageImpl<CalendarEvent>(List.of(event2, event3), pageable, 2)
    );
    
    Page<CalendarEvent> expected = underTest.getAllEventsAfterDate(5L, true, date, 0, 5);
    
    ArgumentCaptor<Long> paramCapture = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Boolean> boolCapture = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<LocalDateTime> dateCapture = ArgumentCaptor.forClass(LocalDateTime.class);
    ArgumentCaptor<Pageable> pageCapture = ArgumentCaptor.forClass(Pageable.class);
    verify(repository).findAllOfAccountAfterDate(paramCapture.capture(), boolCapture.capture(), dateCapture.capture(), pageCapture.capture());
    assertThat(expected.getContent().size()).isEqualTo(2);
    assertThat(paramCapture.getValue()).isEqualTo(5L);
    assertThat(boolCapture.getValue()).isTrue();
    assertThat(dateCapture.getValue()).isEqualTo(date);
    assertThat(pageCapture.getValue()).isEqualTo(pageable);
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
    
    Pageable pageable = PageRequest.of(0, 5);
    LocalDateTime date = LocalDateTime.of(2023, 8, 15, 0, 0);
    LocalDateTime date2 = LocalDateTime.of(2023, 8, 20, 0, 0);
    
    Mockito.when(repository.findAllOfAccountBetweenDates(5L, true, date, date2, pageable))
    .thenReturn(
        new PageImpl<CalendarEvent>(List.of(event2), pageable, 1)
    );
    
    Page<CalendarEvent> expected = underTest.getAllEventsBetweenDates(5L, true, date, date2, 0, 5);
    
    ArgumentCaptor<Long> paramCapture = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Boolean> boolCapture = ArgumentCaptor.forClass(Boolean.class);
    ArgumentCaptor<LocalDateTime> dateCapture = ArgumentCaptor.forClass(LocalDateTime.class);
    ArgumentCaptor<LocalDateTime> dateCapture2 = ArgumentCaptor.forClass(LocalDateTime.class);
    ArgumentCaptor<Pageable> pageCapture = ArgumentCaptor.forClass(Pageable.class);
    verify(repository).findAllOfAccountBetweenDates(paramCapture.capture(), boolCapture.capture(), dateCapture.capture(), dateCapture2.capture(), pageCapture.capture());
    assertThat(expected.getContent().size()).isEqualTo(1);
    assertThat(paramCapture.getValue()).isEqualTo(5L);
    assertThat(boolCapture.getValue()).isTrue();
    assertThat(dateCapture.getValue()).isEqualTo(date);
    assertThat(dateCapture2.getValue()).isEqualTo(date2);
    assertThat(pageCapture.getValue()).isEqualTo(pageable);
  }
}
