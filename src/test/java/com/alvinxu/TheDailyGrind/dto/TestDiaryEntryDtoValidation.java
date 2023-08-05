package com.alvinxu.TheDailyGrind.dto;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestDiaryEntryDtoValidation {
  private DiaryEntryDto dto;
  
  @BeforeEach
  void setup() {
    dto = new DiaryEntryDto();
    dto.setTitle("my title");
    dto.setEntry("my entry");
    dto.setDate_of_entry(LocalDate.of(2023, 4, 20));
  }

  @Test
  void testValidation() {
    
  }
  
  @Test
  void testTitleSizeTooSmall() {
    dto.setTitle("");
  }
  
  @Test
  void testTitleSizeTooLarge() {
    dto.setTitle("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
  }
  
  @Test
  void testDateOfEntryNotNull() {
    dto.setDate_of_entry(null);
  }
  
  @Test
  void testEntryNotBlank() {
    dto.setEntry("");
  }
}
