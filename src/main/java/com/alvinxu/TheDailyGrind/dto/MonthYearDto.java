package com.alvinxu.TheDailyGrind.dto;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;

public class MonthYearDto {
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM")
  private YearMonth monthYear = YearMonth.now();
  
  private int eventPage = 0;
  private int eventSize = 10;
  private int diaryPage = 0;
  private int diarySize = 10;

  public YearMonth getMonthYear() {
    return monthYear;
  }

  public void setMonthYear(YearMonth monthYear) {
    this.monthYear = monthYear;
  }

  public int getEventPage() {
    return eventPage;
  }

  public void setEventPage(int eventPage) {
    this.eventPage = eventPage;
  }

  public int getEventSize() {
    return eventSize;
  }

  public void setEventSize(int eventSize) {
    this.eventSize = eventSize;
  }

  public int getDiaryPage() {
    return diaryPage;
  }

  public void setDiaryPage(int diaryPage) {
    this.diaryPage = diaryPage;
  }

  public int getDiarySize() {
    return diarySize;
  }

  public void setDiarySize(int diarySize) {
    this.diarySize = diarySize;
  }
  
}
