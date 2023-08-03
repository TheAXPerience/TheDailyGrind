package com.alvinxu.TheDailyGrind.models;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TestDiaryEntry {
  @Test
  void testId() {
    DiaryEntry entry = new DiaryEntry();
    
    entry.setId(5L);
    assertThat(entry.getId()).isEqualTo(5L);
  }
  
  @Test
  void testDiaryOwner() {
    DiaryEntry entry = new DiaryEntry();
    Account user = new Account(
        6L,
        "rebecca@black",
        "Rebecca Black",
        "rebeccaBlack<3<3<3",
        LocalDateTime.of(2012, 12, 29, 5, 45),
        "USER"
    );
    
    entry.setDiaryOwner(user);
    assertThat(entry.getDiaryOwner()).isEqualTo(user);
  }
  
  @Test
  void testDateCreated() {
    DiaryEntry entry = new DiaryEntry();
    LocalDateTime date = LocalDateTime.of(2022, 3, 17, 2, 49);
    
    entry.setDateCreated(date);
    assertThat(entry.getDateCreated()).isEqualTo(date);
  }
  
  @Test
  void testDateUpdated() {
    DiaryEntry entry = new DiaryEntry();
    LocalDateTime date = LocalDateTime.of(2022, 3, 17, 2, 49);
    
    entry.setDateUpdated(date);
    assertThat(entry.getDateUpdated()).isEqualTo(date);
  }
  
  @Test
  void testDateOfEntry() {
    DiaryEntry entry = new DiaryEntry();
    LocalDateTime date = LocalDateTime.of(2022, 3, 17, 2, 49);
    
    entry.setDateOfEntry(date);
    assertThat(entry.getDateOfEntry()).isEqualTo(date);
  }
  
  @Test
  void testTitle() {
    DiaryEntry entry = new DiaryEntry();
    String title = "title of the diary entry";
    
    entry.setTitle(title);
    assertThat(entry.getTitle()).isEqualTo(title);
  }
  
  @Test
  void testEntry() {
    DiaryEntry entry = new DiaryEntry();
    String entryText = "omg have you SEEN justin's shoes? they're to DIE for~";
    
    entry.setEntry(entryText);
    assertThat(entry.getEntry()).isEqualTo(entryText);
  }

}
