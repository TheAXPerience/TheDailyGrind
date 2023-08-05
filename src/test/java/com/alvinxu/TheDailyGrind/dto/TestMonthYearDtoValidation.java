package com.alvinxu.TheDailyGrind.dto;

import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;

public class TestMonthYearDtoValidation {
  private MonthYearDto dto;
  
  @BeforeEach
  void setup() {
    dto = new MonthYearDto();
    dto.setMonthYear(YearMonth.of(2023, 8));
    dto.setEventPage(0);
    dto.setEventSize(10);
    dto.setDiaryPage(0);
    dto.setDiarySize(10);
  }
  
  void testValidation() {
    
  }
  
  void testMonthYearNotNull() {
    dto.setMonthYear(null);
  }
}
