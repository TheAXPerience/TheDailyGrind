package com.alvinxu.TheDailyGrind.dto;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;

public class MonthYearDto {
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM")
  private YearMonth monthYear = YearMonth.now();

  public YearMonth getMonthYear() {
    return monthYear;
  }

  public void setMonthYear(YearMonth monthYear) {
    this.monthYear = monthYear;
  }
  
  
}
