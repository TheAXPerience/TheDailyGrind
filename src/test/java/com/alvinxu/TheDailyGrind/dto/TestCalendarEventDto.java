package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TestCalendarEventDto {
  @Test
  void testDateOfEvent() {
    CalendarEventDto dto = new CalendarEventDto();
    LocalDateTime date = LocalDateTime.of(2015, 4, 20, 6, 9);
    
    dto.setDateOfEvent(date);
    assertThat(dto.getDateOfEvent()).isEqualTo(date);
  }
  
  @Test
  void testTitle() {
    CalendarEventDto dto = new CalendarEventDto();
    String title = "title of the event";
    
    dto.setTitle(title);
    assertThat(dto.getTitle()).isEqualTo(title);
  }
  
  @Test
  void testDescription() {
    CalendarEventDto dto = new CalendarEventDto();
    String description = "description of the event";
    
    dto.setDescription(description);
    assertThat(dto.getDescription()).isEqualTo(description);
  }
  
  @Test
  void testPublic() {
    CalendarEventDto dto = new CalendarEventDto();
    
    dto.setPublicToggle(false);
    assertThat(dto.getPublicToggle()).isFalse();
    
    dto.setPublicToggle(true);
    assertThat(dto.getPublicToggle()).isTrue();
  }
  
  @Test
  void testComplete() {
    CalendarEventDto dto = new CalendarEventDto();
    
    dto.setCompleteToggle(false);
    assertThat(dto.getCompleteToggle()).isFalse();
    
    dto.setCompleteToggle(true);
    assertThat(dto.getCompleteToggle()).isTrue();
  }
}
