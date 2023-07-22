package com.alvinxu.TheDailyGrind.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alvinxu.TheDailyGrind.dto.CalendarEventDto;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.CalendarEvent;
import com.alvinxu.TheDailyGrind.repositories.CalendarEventRepository;

import jakarta.transaction.Transactional;

@Service
public class CalendarEventService {
	@Autowired
	CalendarEventRepository calendarEventRepository;
	
	@Autowired
	AccountService accountService;
	
	@Transactional
	public boolean saveNewCalendarEvent(CalendarEventDto dto, String userEmail) {
		Account user = accountService.getAccountByEmail(userEmail);
		if (user == null) {
			return false;
		}
		
		CalendarEvent cevent = new CalendarEvent();
		cevent.setEventOrganizer(user);
		cevent.setDateOfEvent(dto.getDate_of_event());
		cevent.setTitle(dto.getEvent_name());
		cevent.setDescription(dto.getDescription());
		cevent.setPublic(dto.isIs_public());
		this.calendarEventRepository.save(cevent);
		
		return true;
	}
	
	public List<CalendarEvent> getAllEventsOfAccount(Account user) {
		return this.calendarEventRepository.findAllByAccountId(user.getId());
	}
}
