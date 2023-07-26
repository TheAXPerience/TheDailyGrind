package com.alvinxu.TheDailyGrind.services;

import java.util.List;

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
	public boolean saveNewDiaryEntry(DiaryEntryDto dto, String userEmail) {
		Account user = accountService.getAccountByEmail(userEmail);
		if (user == null) {
			return false;
		}
		
		DiaryEntry dentry = new DiaryEntry();
		dentry.setDiaryOwner(user);
		dentry.setTitle(dto.getTitle());
		dentry.setEntry(dto.getEntry());
		dentry.setDateOfEntry(dto.getDate_of_entry());
		this.diaryEntryRepository.save(dentry);
		
		return true;
	}
	
	public List<DiaryEntry> getAllEventsOfAccount(Account user) {
		return this.diaryEntryRepository.findAllByAccountId(user.getId());
	}
}
