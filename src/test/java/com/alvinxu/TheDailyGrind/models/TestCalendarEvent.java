package com.alvinxu.TheDailyGrind.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TestCalendarEvent {
  @Test
  void testId() {
    // with
    CalendarEvent event = new CalendarEvent();
    
    // when
    event.setId(2L);
    
    // then
    assertThat(event.getId()).isEqualTo(2L);
  }
  
  @Test
  void testEventOrganizer() {
    // with
    CalendarEvent event = new CalendarEvent();
    Account user = new Account(
        1L,
        "hello@world",
        "username",
        "password",
        LocalDateTime.of(2021, 5, 20, 4, 20),
        "USER"
    );
    
    // when
    event.setEventOrganizer(user);
    
    // then
    // this is why we test Account.equals()
    assertThat(event.getEventOrganizer()).isEqualTo(user);
  }
  
  @Test
  void testDateCreated() {
    // with
    CalendarEvent event = new CalendarEvent();
    LocalDateTime date = LocalDateTime.of(2022, 3, 17, 2, 49);
    
    // when
    event.setDateCreated(date);
    
    // then
    assertThat(event.getDateCreated()).isEqualTo(date);
  }
  
  @Test
  void testDateUpdated() {
    // with
    CalendarEvent event = new CalendarEvent();
    LocalDateTime date = LocalDateTime.of(2022, 3, 17, 2, 49);
    
    // when
    event.setDateUpdated(date);
    
    // then
    assertThat(event.getDateUpdated()).isEqualTo(date);
  }
  
  @Test
  void testDateOfEvent() {
    // with
    CalendarEvent event = new CalendarEvent();
    LocalDateTime date = LocalDateTime.of(2022, 3, 17, 2, 49);
    
    // when
    event.setDateOfEvent(date);
    
    // then
    assertThat(event.getDateOfEvent()).isEqualTo(date);
  }
  
  @Test
  void testTitle() {
    // with
    CalendarEvent event = new CalendarEvent();
    String title = "hello world";
    
    // when
    event.setTitle(title);
    
    // then
    assertThat(event.getTitle()).isEqualTo(title);
  }
  
  @Test
  void testDescription() {
    // with
    CalendarEvent event = new CalendarEvent();
    String description = "this is the description of an event. ::)))";
    
    // when
    event.setDescription(description);
    
    // then
    assertThat(event.getDescription()).isEqualTo(description);
  }
  
  @Test
  void testIsPublic() {
    CalendarEvent event = new CalendarEvent();
    
    event.setPublic(true);
    assertThat(event.isPublic()).isTrue();
    
    event.setPublic(false);
    assertThat(event.isPublic()).isFalse();
  }
  
  @Test
  void testIsComplete() {
    CalendarEvent event = new CalendarEvent();
    
    event.setComplete(true);
    assertThat(event.isComplete()).isTrue();
    
    event.setComplete(false);
    assertThat(event.isComplete()).isFalse();
  }
}
