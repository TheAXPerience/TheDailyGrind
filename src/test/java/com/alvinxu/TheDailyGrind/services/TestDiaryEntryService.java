package com.alvinxu.TheDailyGrind.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alvinxu.TheDailyGrind.dto.DiaryEntryDto;
import com.alvinxu.TheDailyGrind.exceptions.EventEntryNotFoundException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.DiaryEntry;
import com.alvinxu.TheDailyGrind.repositories.DiaryEntryRepository;

@ExtendWith(MockitoExtension.class)
public class TestDiaryEntryService {
  @Mock private DiaryEntryRepository repository;
  @Mock private AccountService accounts;
  private DiaryEntryService underTest;
  
  @BeforeEach
  void setup() {
    underTest = new DiaryEntryService(accounts, repository);
  }
  
  @Test
  void testGetById() {
    DiaryEntry entry = new DiaryEntry();
    entry.setId(5L);
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(entry));
    
    DiaryEntry expected = underTest.getById(5L);
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(repository).findById(idCaptor.capture());
    assertThat(expected).isEqualTo(entry);
  }
  
  @Test
  void testGetByIdDoesNotExist() {
    Mockito.when(repository.findById(5L)).thenReturn(Optional.empty());
    
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
    
    Mockito.when(accounts.getAccountByEmail(email)).thenReturn(user);
    
    underTest.saveNewDiaryEntry(dto, email);
    
    ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<DiaryEntry> entryCaptor = ArgumentCaptor.forClass(DiaryEntry.class);
    
    verify(accounts).getAccountByEmail(emailCaptor.capture());
    verify(repository).save(entryCaptor.capture());
    assertThat(emailCaptor.getValue()).isEqualTo(email);
    assertThat(entryCaptor.getValue().getTitle()).isEqualTo(dto.getTitle());
    assertThat(entryCaptor.getValue().getEntry()).isEqualTo(dto.getEntry());
    assertThat(entryCaptor.getValue().getDateOfEntry()).isEqualTo(dto.getDate_of_entry().atStartOfDay());
  }
  
  @Test
  void testSaveNewDiaryEntryBadUser() {
    DiaryEntryDto dto = new DiaryEntryDto();
    dto.setTitle("title");
    dto.setEntry("description");
    dto.setDate_of_entry(LocalDate.of(2023, 8, 14));
    
    String email = "hello@world";
    
    Mockito.when(accounts.getAccountByEmail(email)).thenReturn(null);
    
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
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("old title");
    entry.setEntry("old description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(entry));
    
    underTest.updateDiaryEntry(5L, email, dto);
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<DiaryEntry> entryCaptor = ArgumentCaptor.forClass(DiaryEntry.class);
    
    verify(repository).findById(idCaptor.capture());
    verify(repository).save(entryCaptor.capture());
    
    assertThat(idCaptor.getValue()).isEqualTo(5L);
    assertThat(entryCaptor.getValue().getTitle()).isEqualTo(dto.getTitle());
    assertThat(entryCaptor.getValue().getDateOfEntry()).isEqualTo(dto.getDate_of_entry().atStartOfDay());
    assertThat(entryCaptor.getValue().getEntry()).isEqualTo(dto.getEntry());
  }
  
  @Test
  void testUpdateCalendarEventDoesNotExist() {
    DiaryEntryDto dto = new DiaryEntryDto();
    dto.setTitle("title");
    dto.setEntry("description");
    dto.setDate_of_entry(LocalDate.of(2023, 8, 14));
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.empty());
    
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
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("title");
    entry.setEntry("description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(entry));
    
    assertThatThrownBy(() -> underTest.updateDiaryEntry(5L, "hello@jank", dto))
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
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("title");
    entry.setEntry("description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(entry));
    
    underTest.deleteDiaryEntry(5L, email);
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<DiaryEntry> entryCaptor = ArgumentCaptor.forClass(DiaryEntry.class);
    
    verify(repository).findById(idCaptor.capture());
    verify(repository).delete(entryCaptor.capture());
    
    assertThat(idCaptor.getValue()).isEqualTo(5L);
    assertThat(entryCaptor.getValue()).isEqualTo(entry);
  }
  
  @Test
  void testDeleteDiaryEntryDoesNotExist() {
    Mockito.when(repository.findById(5L)).thenReturn(Optional.empty());
    
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
    
    DiaryEntry entry = new DiaryEntry();
    entry.setDiaryOwner(user);
    entry.setTitle("title");
    entry.setEntry("description");
    entry.setDateOfEntry(LocalDateTime.of(2023, 8, 15, 6, 40));
    
    Mockito.when(repository.findById(5L)).thenReturn(Optional.of(entry));
    
    assertThatThrownBy(() -> underTest.deleteDiaryEntry(5L, "hello@jank"))
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
    
    Pageable pageable = PageRequest.of(0, 5);
    
    Mockito.when(repository.findAllByAccountId(5L, pageable)).thenReturn(
        new PageImpl<DiaryEntry>(List.of(entry1, entry2, entry3), pageable, 3)
    );
    
    Page<DiaryEntry> expected = underTest.getAllEntriesOfAccount(5L, 0, 5);
    
    ArgumentCaptor<Long> paramCapture = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Pageable> pageCapture = ArgumentCaptor.forClass(Pageable.class);
    verify(repository).findAllByAccountId(paramCapture.capture(), pageCapture.capture());
    assertThat(expected.getContent().size()).isEqualTo(3);
    assertThat(paramCapture.getValue()).isEqualTo(5L);
    assertThat(pageCapture.getValue()).isEqualTo(pageable);
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
    
    Pageable pageable = PageRequest.of(0, 5);
    LocalDateTime date = LocalDateTime.of(2023, 8, 20, 0, 0);
    
    Mockito.when(repository.findAllOfAccountBeforeDate(5L, date, pageable))
    .thenReturn(
        new PageImpl<DiaryEntry>(List.of(entry1, entry2), pageable, 2)
    );
    
    Page<DiaryEntry> expected = underTest.getAllEntriesBeforeDate(5L, date, 0, 5);
    
    ArgumentCaptor<Long> paramCapture = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<LocalDateTime> dateCapture = ArgumentCaptor.forClass(LocalDateTime.class);
    ArgumentCaptor<Pageable> pageCapture = ArgumentCaptor.forClass(Pageable.class);
    verify(repository).findAllOfAccountBeforeDate(paramCapture.capture(), dateCapture.capture(), pageCapture.capture());
    assertThat(expected.getContent().size()).isEqualTo(2);
    assertThat(paramCapture.getValue()).isEqualTo(5L);
    assertThat(dateCapture.getValue()).isEqualTo(date);
    assertThat(pageCapture.getValue()).isEqualTo(pageable);
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
    
    Pageable pageable = PageRequest.of(0, 5);
    LocalDateTime date = LocalDateTime.of(2023, 8, 15, 0, 0);
    LocalDateTime date2 = LocalDateTime.of(2023, 8, 20, 0, 0);
    
    Mockito.when(repository.findAllOfAccountBetweenDates(5L, date, date2, pageable))
    .thenReturn(
        new PageImpl<DiaryEntry>(List.of(entry2), pageable, 1)
    );
    
    Page<DiaryEntry> expected = underTest.getAllEntriesBetweenDates(5L, date, date2, 0, 5);
    
    ArgumentCaptor<Long> paramCapture = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<LocalDateTime> dateCapture = ArgumentCaptor.forClass(LocalDateTime.class);
    ArgumentCaptor<LocalDateTime> dateCapture2 = ArgumentCaptor.forClass(LocalDateTime.class);
    ArgumentCaptor<Pageable> pageCapture = ArgumentCaptor.forClass(Pageable.class);
    verify(repository).findAllOfAccountBetweenDates(paramCapture.capture(), dateCapture.capture(), dateCapture2.capture(), pageCapture.capture());
    assertThat(expected.getContent().size()).isEqualTo(1);
    assertThat(paramCapture.getValue()).isEqualTo(5L);
    assertThat(dateCapture.getValue()).isEqualTo(date);
    assertThat(dateCapture2.getValue()).isEqualTo(date2);
    assertThat(pageCapture.getValue()).isEqualTo(pageable);
  }
}
