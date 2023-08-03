package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TestDiaryEntryDto {
  @Test
  void testTitle() {
    DiaryEntryDto dto = new DiaryEntryDto();
    String title = "title of my life";
    
    dto.setTitle(title);
    assertThat(dto.getTitle()).isEqualTo(title);
  }
  
  @Test
  void testDateOfEntry() {
    DiaryEntryDto dto = new DiaryEntryDto();
    LocalDate date = LocalDate.of(2022, 4, 20);
    
    dto.setDate_of_entry(date);
    assertThat(dto.getDate_of_entry()).isEqualTo(date);
  }
  
  @Test
  void testEntry() {
    DiaryEntryDto dto = new DiaryEntryDto();
    String entry = "so anyways, beaver butthead tried to ask jonah genevieve out, but she rejected him immediately, like omg!";
    
    dto.setEntry(entry);
    assertThat(dto.getEntry()).isEqualTo(entry);
  }
}
