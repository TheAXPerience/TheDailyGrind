package com.alvinxu.TheDailyGrind.dto;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCalendarEventDtoValidation {
  private CalendarEventDto dto;
  
  @BeforeEach
  void setup() {
    dto = new CalendarEventDto();
    dto.setTitle("my title");
    dto.setDescription("my description");
    dto.setDateOfEvent(LocalDateTime.of(2023, 9, 30, 5, 16));
    dto.setPublicToggle(true);
    dto.setCompleteToggle(false);
  }

  @Test
  void testValidation() {
    
  }
  
  @Test
  void testDateOfEventNotNull() {
    dto.setDateOfEvent(null);
  }
  
  @Test
  void testTitleSizeTooSmall() {
    dto.setTitle("");
  }
  
  @Test
  void testTitleSizeTooLarge() {
    dto.setTitle("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
  }
}
