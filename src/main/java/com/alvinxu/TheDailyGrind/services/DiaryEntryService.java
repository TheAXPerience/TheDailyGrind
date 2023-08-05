package com.alvinxu.TheDailyGrind.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alvinxu.TheDailyGrind.dto.DiaryEntryDto;
import com.alvinxu.TheDailyGrind.exceptions.EventEntryNotFoundException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.DiaryEntry;
import com.alvinxu.TheDailyGrind.repositories.DiaryEntryRepository;

import jakarta.transaction.Transactional;

@Service
public class DiaryEntryService {
	private DiaryEntryRepository diaryEntryRepository;
	private AccountService accountService;
	
	public DiaryEntryService(AccountService accountService, DiaryEntryRepository diaryEntryRepository) {
	  this.accountService = accountService;
	  this.diaryEntryRepository = diaryEntryRepository;
	}
	
	@Transactional
	public DiaryEntry getById(Long deid) {
	  Optional<DiaryEntry> dentry = diaryEntryRepository.findById(deid);
	  if (dentry.isEmpty()) {
	    throw new EventEntryNotFoundException("Error: diary entry does not exist.");
	  }
	  return dentry.get();
	}
	
	@Transactional
	public void saveNewDiaryEntry(DiaryEntryDto dto, String userEmail) {
		Account user = accountService.getAccountByEmail(userEmail);
		if (user == null) {
			throw new UsernameNotFoundException("Error: could not verify user.");
		}
		
		DiaryEntry dentry = new DiaryEntry();
		dentry.setDiaryOwner(user);
		dentry.setTitle(dto.getTitle());
		dentry.setEntry(dto.getEntry());
		dentry.setDateOfEntry(dto.getDate_of_entry().atStartOfDay());
		this.diaryEntryRepository.save(dentry);
	}
	
	@Transactional
	public Page<DiaryEntry> getAllEntriesOfAccount(
	    Long userId,
	    int page,
	    int size
	) {
	  Pageable pageable = PageRequest.of(page, size);
		return this.diaryEntryRepository.findAllByAccountId(userId, pageable);
	}
	
  @Transactional
  public void updateDiaryEntry(Long deid, String userEmail, DiaryEntryDto dto) throws IllegalArgumentException, IllegalAccessException {
    Optional<DiaryEntry> diantry = this.diaryEntryRepository.findById(deid);
    if (diantry.isEmpty()) {
      throw new EventEntryNotFoundException("Error: diary entry does not exist.");
    }
    
    DiaryEntry dentry = diantry.get();
    if (!dentry.getDiaryOwner().getEmail().equals(userEmail)) {
      throw new IllegalAccessException("Error: user does not have permission to edit diary entry.");
    }
    
    dentry.setDateOfEntry(dto.getDate_of_entry().atStartOfDay());
    dentry.setTitle(dto.getTitle());
    dentry.setEntry(dto.getEntry());
    this.diaryEntryRepository.save(dentry);
  }
	
	@Transactional
	public void deleteDiaryEntry(Long deid, String userEmail) throws IllegalArgumentException, IllegalAccessException {
	  Optional<DiaryEntry> diantry = this.diaryEntryRepository.findById(deid);
	  if (diantry.isEmpty()) {
	    throw new EventEntryNotFoundException("Error: diary entry does not exist.");
	  }
	  
	  DiaryEntry dentry = diantry.get();
	  if (!dentry.getDiaryOwner().getEmail().equals(userEmail)) {
	    throw new IllegalAccessException("Error: user does not have permission to delete diary entry.");
	  }
	  
	  this.diaryEntryRepository.delete(dentry);
	}
	
  @Transactional
  public Page<DiaryEntry> getAllEntriesBeforeDate(
      Long userId,
      LocalDateTime date,
      int page,
      int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    return this.diaryEntryRepository.findAllOfAccountBeforeDate(
        userId, date, pageable);
  }
  
  @Transactional
  public Page<DiaryEntry> getAllEntriesBetweenDates(
      Long userId,
      LocalDateTime begin,
      LocalDateTime end,
      int page,
      int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    return this.diaryEntryRepository.findAllOfAccountBetweenDates(
        userId, begin, end, pageable);
  }
}
