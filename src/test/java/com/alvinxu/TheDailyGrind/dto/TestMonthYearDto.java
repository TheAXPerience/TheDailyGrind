package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

public class TestMonthYearDto {
  @Test
  void testMonthYear() {
    MonthYearDto dto = new MonthYearDto();
    YearMonth date = YearMonth.of(2012, 7);
    
    dto.setMonthYear(date);
    assertThat(dto.getMonthYear()).isEqualTo(date);
  }
  
  @Test
  void testEventPage() {
    MonthYearDto dto = new MonthYearDto();
    
    dto.setEventPage(7);
    assertThat(dto.getEventPage()).isEqualTo(7);
  }
  
  @Test
  void testEventSize() {
    MonthYearDto dto = new MonthYearDto();
    
    dto.setEventSize(11);
    assertThat(dto.getEventSize()).isEqualTo(11);
  }
  
  @Test
  void testDiaryPage() {
    MonthYearDto dto = new MonthYearDto();
    
    dto.setDiaryPage(13);
    assertThat(dto.getDiaryPage()).isEqualTo(13);
  }
  
  @Test
  void testDiarySize() {
    MonthYearDto dto = new MonthYearDto();
    
    dto.setDiarySize(25);
    assertThat(dto.getDiarySize()).isEqualTo(25);
  }
}
