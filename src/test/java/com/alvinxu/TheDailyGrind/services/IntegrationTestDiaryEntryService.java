package com.alvinxu.TheDailyGrind.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.alvinxu.TheDailyGrind.dto.DiaryEntryDto;
import com.alvinxu.TheDailyGrind.exceptions.EventEntryNotFoundException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.DiaryEntry;
import com.alvinxu.TheDailyGrind.repositories.AccountRepository;
import com.alvinxu.TheDailyGrind.repositories.DiaryEntryRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTestDiaryEntryService {
  @Autowired private DiaryEntryRepository repository;
  @Autowired private AccountRepository accRepository;
  private AccountService accounts;
  private DiaryEntryService underTest;
  
  @BeforeAll
  void setup() {
    // autowiring doesn't work???
    accounts = new AccountService(accRepository, new BCryptPasswordEncoder());
    underTest = new DiaryEntryService(accounts, repository);
  }
  
  @AfterEach
  void teardown() {
    repository.deleteAll();
    accRepository.deleteAll();
  }
  
  @Test
  void testGetById() {
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail("hello@world");
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("title");
    entry.setEntry("description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    repository.save(entry);
    
    DiaryEntry expected = underTest.getById(entry.getId());
    
    assertThat(expected).isEqualTo(entry);
  }
  
  @Test
  void testGetByIdDoesNotExist() {
    assertThatThrownBy(() -> underTest.getById(5L))
        .isInstanceOf(EventEntryNotFoundException.class)
        .hasMessageContaining("Error: diary entry does not exist.");
  }
  
  @Test
  void testSaveNewDiaryEntry() {
    DiaryEntryDto dto = new DiaryEntryDto();
    dto.setTitle("title");
    dto.setEntry("description");
    dto.setDate_of_entry(LocalDate.of(2023, 8, 14));
    
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    underTest.saveNewDiaryEntry(dto, email);
    
    List<DiaryEntry> entries = repository.findAll();
    assertThat(entries.size()).isEqualTo(1);
    
    DiaryEntry entry = entries.get(0);
    assertThat(entry.getDiaryOwner()).isEqualTo(user);
    assertThat(entry.getTitle()).isEqualTo(dto.getTitle());
    assertThat(entry.getEntry()).isEqualTo(dto.getEntry());
    assertThat(entry.getDateOfEntry()).isEqualTo(dto.getDate_of_entry().atStartOfDay());
  }
  
  @Test
  void testSaveNewDiaryEntryBadUser() {
    DiaryEntryDto dto = new DiaryEntryDto();
    dto.setTitle("title");
    dto.setEntry("description");
    dto.setDate_of_entry(LocalDate.of(2023, 8, 14));
    
    String email = "hello@world";
    
    assertThatThrownBy(() -> underTest.saveNewDiaryEntry(dto, email))
    .isInstanceOf(UsernameNotFoundException.class)
    .hasMessageContaining("Error: could not verify user.");
  }
  
  @Test
  void testUpdateDiaryEntry() throws EventEntryNotFoundException, IllegalAccessException {
    DiaryEntryDto dto = new DiaryEntryDto();
    dto.setTitle("title");
    dto.setEntry("description");
    dto.setDate_of_entry(LocalDate.of(2023, 8, 14));
    
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("old title");
    entry.setEntry("old description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    repository.save(entry);
    
    underTest.updateDiaryEntry(entry.getId(), email, dto);
    
    Optional<DiaryEntry> expected = repository.findById(entry.getId());
    assertThat(expected.isEmpty()).isFalse();
    
    assertThat(expected.get().getTitle()).isEqualTo(dto.getTitle());
    assertThat(expected.get().getDateOfEntry()).isEqualTo(dto.getDate_of_entry().atStartOfDay());
    assertThat(expected.get().getEntry()).isEqualTo(dto.getEntry());
  }
  
  @Test
  void testUpdateCalendarEventDoesNotExist() {
    DiaryEntryDto dto = new DiaryEntryDto();
    dto.setTitle("title");
    dto.setEntry("description");
    dto.setDate_of_entry(LocalDate.of(2023, 8, 14));
    
    assertThatThrownBy(() -> underTest.updateDiaryEntry(5L, "hello@world", dto))
        .isInstanceOf(EventEntryNotFoundException.class)
        .hasMessageContaining("Error: diary entry does not exist.");
  }
  
  @Test
  void testUpdateDiaryEntryBadUser() {
    DiaryEntryDto dto = new DiaryEntryDto();
    dto.setTitle("title");
    dto.setEntry("description");
    dto.setDate_of_entry(LocalDate.of(2023, 8, 14));
    
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("title");
    entry.setEntry("description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    repository.save(entry);
    
    assertThatThrownBy(() -> underTest.updateDiaryEntry(entry.getId(), "hello@jank", dto))
        .isInstanceOf(IllegalAccessException.class)
        .hasMessageContaining("Error: user does not have permission to edit diary entry.");
  }
  
  @Test
  void testDeleteDiaryEntry() throws EventEntryNotFoundException, IllegalAccessException {
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("title");
    entry.setEntry("description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    repository.save(entry);
    
    underTest.deleteDiaryEntry(entry.getId(), email);
    
    Optional<DiaryEntry> expected = repository.findById(entry.getId());
    assertThat(expected.isEmpty()).isTrue();
  }
  
  @Test
  void testDeleteDiaryEntryDoesNotExist() {
    assertThatThrownBy(() -> underTest.deleteDiaryEntry(5L, "hello@world"))
        .isInstanceOf(EventEntryNotFoundException.class)
        .hasMessageContaining("Error: diary entry does not exist.");
  }
  
  @Test
  void testDeleteDiaryEntryBadUser() {
    String email = "hello@world";
    Account user = new Account();
    user.setUsername("username");
    user.setPassword("password");
    user.setEmail(email);
    user.setDateOfBirth(LocalDateTime.of(2016, 5, 14, 19, 7));
    user.setAuthority("USER");
    accRepository.save(user);
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("title");
    entry.setEntry("description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    repository.save(entry);
    
    assertThatThrownBy(() -> underTest.deleteDiaryEntry(entry.getId(), "hello@jank"))
        .isInstanceOf(IllegalAccessException.class)
        .hasMessageContaining("Error: user does not have permission to delete diary entry.");
  }
  
  @Test
  void testGetAllEventsOfAccount() {
    Account user1 = new Account();
    user1.setUsername("username1");
    user1.setPassword("password1");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 4, 20, 15, 36));
    user1.setAuthority("USER");
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2014, 6, 9, 13, 56));
    user2.setAuthority("USER");
    
    DiaryEntry entry1 = new DiaryEntry();
    entry1.setDiaryOwner(user1);
    entry1.setTitle("title1");
    entry1.setEntry("description");
    entry1.setDateOfEntry(LocalDateTime.of(2023, 8, 13, 5, 7));
    
    DiaryEntry entry2 = new DiaryEntry();
    entry2.setDiaryOwner(user1);
    entry2.setTitle("title2");
    entry2.setEntry("description");
    entry2.setDateOfEntry(LocalDateTime.of(2023, 8, 19, 5, 7));
    
    DiaryEntry entry3 = new DiaryEntry();
    entry3.setDiaryOwner(user1);
    entry3.setTitle("title3");
    entry3.setEntry("description");
    entry3.setDateOfEntry(LocalDateTime.of(2023, 9, 10, 5, 7));
    
    DiaryEntry entry4 = new DiaryEntry();
    entry4.setDiaryOwner(user2);
    entry4.setTitle("title4");
    entry4.setEntry("description");
    entry4.setDateOfEntry(LocalDateTime.of(2023, 8, 12, 5, 7));
    
    accRepository.save(user1);
    accRepository.save(user2);
    repository.save(entry1);
    repository.save(entry2);
    repository.save(entry3);
    repository.save(entry4);
    
    Page<DiaryEntry> expected = underTest.getAllEntriesOfAccount(user1.getId(), 0, 5);
    
    List<DiaryEntry> content = expected.getContent();
    assertThat(content.size()).isEqualTo(3);
    assertThat(content.contains(entry1)).isTrue();
    assertThat(content.contains(entry2)).isTrue();
    assertThat(content.contains(entry3)).isTrue();
    assertThat(content.contains(entry4)).isFalse();
  }
  
  @Test
  void testGetAllEventsAfterDate() {
    Account user1 = new Account();
    user1.setUsername("username1");
    user1.setPassword("password1");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 4, 20, 15, 36));
    user1.setAuthority("USER");
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2014, 6, 9, 13, 56));
    user2.setAuthority("USER");
    
    DiaryEntry entry1 = new DiaryEntry();
    entry1.setDiaryOwner(user1);
    entry1.setTitle("title1");
    entry1.setEntry("description");
    entry1.setDateOfEntry(LocalDateTime.of(2023, 8, 13, 5, 7));
    
    DiaryEntry entry2 = new DiaryEntry();
    entry2.setDiaryOwner(user1);
    entry2.setTitle("title2");
    entry2.setEntry("description");
    entry2.setDateOfEntry(LocalDateTime.of(2023, 8, 19, 5, 7));
    
    DiaryEntry entry3 = new DiaryEntry();
    entry3.setDiaryOwner(user1);
    entry3.setTitle("title3");
    entry3.setEntry("description");
    entry3.setDateOfEntry(LocalDateTime.of(2023, 9, 10, 5, 7));
    
    DiaryEntry entry4 = new DiaryEntry();
    entry4.setDiaryOwner(user2);
    entry4.setTitle("title4");
    entry4.setEntry("description");
    entry4.setDateOfEntry(LocalDateTime.of(2023, 8, 12, 5, 7));
    
    accRepository.save(user1);
    accRepository.save(user2);
    repository.save(entry1);
    repository.save(entry2);
    repository.save(entry3);
    repository.save(entry4);
    
    LocalDateTime date = LocalDateTime.of(2023, 8, 20, 0, 0);
    
    Page<DiaryEntry> expected = underTest.getAllEntriesBeforeDate(user1.getId(), date, 0, 5);
    
    List<DiaryEntry> content = expected.getContent();
    assertThat(content.size()).isEqualTo(2);
    assertThat(content.contains(entry1)).isTrue();
    assertThat(content.contains(entry2)).isTrue();
    assertThat(content.contains(entry3)).isFalse();
    assertThat(content.contains(entry4)).isFalse();
  }
  
  @Test
  void testGetAllEventsBetweenDates() {
    Account user1 = new Account();
    user1.setUsername("username1");
    user1.setPassword("password1");
    user1.setEmail("hello@world");
    user1.setDateOfBirth(LocalDateTime.of(2012, 4, 20, 15, 36));
    user1.setAuthority("USER");
    
    Account user2 = new Account();
    user2.setUsername("username2");
    user2.setPassword("password2");
    user2.setEmail("hello@jank");
    user2.setDateOfBirth(LocalDateTime.of(2014, 6, 9, 13, 56));
    user2.setAuthority("USER");
    
    DiaryEntry entry1 = new DiaryEntry();
    entry1.setDiaryOwner(user1);
    entry1.setTitle("title1");
    entry1.setEntry("description");
    entry1.setDateOfEntry(LocalDateTime.of(2023, 8, 13, 5, 7));
    
    DiaryEntry entry2 = new DiaryEntry();
    entry2.setDiaryOwner(user1);
    entry2.setTitle("title2");
    entry2.setEntry("description");
    entry2.setDateOfEntry(LocalDateTime.of(2023, 8, 19, 5, 7));
    
    DiaryEntry entry3 = new DiaryEntry();
    entry3.setDiaryOwner(user1);
    entry3.setTitle("title3");
    entry3.setEntry("description");
    entry3.setDateOfEntry(LocalDateTime.of(2023, 9, 10, 5, 7));
    
    DiaryEntry entry4 = new DiaryEntry();
    entry4.setDiaryOwner(user2);
    entry4.setTitle("title4");
    entry4.setEntry("description");
    entry4.setDateOfEntry(LocalDateTime.of(2023, 8, 12, 5, 7));
    
    accRepository.save(user1);
    accRepository.save(user2);
    repository.save(entry1);
    repository.save(entry2);
    repository.save(entry3);
    repository.save(entry4);
    
    LocalDateTime date = LocalDateTime.of(2023, 8, 15, 0, 0);
    LocalDateTime date2 = LocalDateTime.of(2023, 8, 20, 0, 0);
    
    Page<DiaryEntry> expected = underTest.getAllEntriesBetweenDates(user1.getId(), date, date2, 0, 5);
    
    List<DiaryEntry> content = expected.getContent();
    assertThat(content.size()).isEqualTo(1);
    assertThat(content.contains(entry1)).isFalse();
    assertThat(content.contains(entry2)).isTrue();
    assertThat(content.contains(entry3)).isFalse();
    assertThat(content.contains(entry4)).isFalse();
  }
}
