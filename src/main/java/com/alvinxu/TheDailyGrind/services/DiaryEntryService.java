package com.alvinxu.TheDailyGrind.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alvinxu.TheDailyGrind.dto.DiaryEntryDto;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.DiaryEntry;
import com.alvinxu.TheDailyGrind.repositories.DiaryEntryRepository;

import jakarta.transaction.Transactional;

@Service
public class DiaryEntryService {
	@Autowired
	DiaryEntryRepository diaryEntryRepository;
	
	@Autowired
	AccountService accountService;
	
	@Transactional
	public DiaryEntry getById(Long deid) {
	  Optional<DiaryEntry> dentry = diaryEntryRepository.findById(deid);
	  if (dentry.isEmpty()) {
	    throw new IllegalArgumentException("Error: diary entry does not exist");
	  }
	  return dentry.get();
	}
	
	@Transactional
	public void saveNewDiaryEntry(DiaryEntryDto dto, String userEmail) {
		Account user = accountService.getAccountByEmail(userEmail);
		if (user == null) {
			throw new IllegalArgumentException("Error: could not verify user");
		}
		
		DiaryEntry dentry = new DiaryEntry();
		dentry.setDiaryOwner(user);
		dentry.setTitle(dto.getTitle());
		dentry.setEntry(dto.getEntry());
		dentry.setDateOfEntry(dto.getDate_of_entry().atStartOfDay());
		this.diaryEntryRepository.save(dentry);
	}
	
	@Transactional
	public List<DiaryEntry> getAllEventsOfAccount(Long userId) {
		return this.diaryEntryRepository.findAllByAccountId(userId);
	}
	
	// TODO
	@Transactional
	public List<DiaryEntry> getAllEntriesBeforeDate(Long userId, LocalDateTime date) {
	  return null;
	}
	
  @Transactional
  public void updateDiaryEntry(Long deid, String userEmail, DiaryEntryDto dto) throws IllegalArgumentException {
    Optional<DiaryEntry> diantry = this.diaryEntryRepository.findById(deid);
    if (diantry.isEmpty()) {
      throw new IllegalArgumentException("Error: could not find diary entry");
    }
    
    DiaryEntry dentry = diantry.get();
    if (!dentry.getDiaryOwner().getEmail().equals(userEmail)) {
      throw new IllegalArgumentException("Error: user does not have permission to alter diary entry");
    }
    
    dentry.setDateOfEntry(dto.getDate_of_entry().atStartOfDay());
    dentry.setTitle(dto.getTitle());
    dentry.setEntry(dto.getEntry());
    this.diaryEntryRepository.save(dentry);
  }
	
	@Transactional
	public void deleteDiaryEntry(Long deid, String userEmail) throws IllegalArgumentException {
	  Optional<DiaryEntry> diantry = this.diaryEntryRepository.findById(deid);
	  if (diantry.isEmpty()) {
	    throw new IllegalArgumentException("Error: could not find diary entry");
	  }
	  
	  DiaryEntry dentry = diantry.get();
	  if (!dentry.getDiaryOwner().getEmail().equals(userEmail)) {
	    throw new IllegalArgumentException("Error: user does not have permission to delete diary entry");
	  }
	  
	  this.diaryEntryRepository.delete(dentry);
	}
	
}
