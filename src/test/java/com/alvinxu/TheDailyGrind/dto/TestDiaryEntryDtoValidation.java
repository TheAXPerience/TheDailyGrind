package com.alvinxu.TheDailyGrind.dto;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;

public class TestDiaryEntryDtoValidation {
  private DiaryEntryDto dto;
  
  @BeforeEach
  void setup() {
    dto = new DiaryEntryDto();
    dto.setTitle("my title");
    dto.setEntry("my entry");
    dto.setDate_of_entry(LocalDate.of(2023, 4, 20));
  }

  void testValidation() {
    
  }
  
  void testTitleSizeTooSmall() {
    dto.setTitle("");
  }
  
  void testTitleSizeTooLarge() {
    dto.setTitle("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
  }
  
  void testDateOfEntryNotNull() {
    dto.setDate_of_entry(null);
  }
  
  void testEntryNotBlank() {
    dto.setEntry("");
  }
}
