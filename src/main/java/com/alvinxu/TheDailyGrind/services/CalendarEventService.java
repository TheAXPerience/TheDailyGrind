package com.alvinxu.TheDailyGrind.services;

import java.time.LocalDateTime;
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
	
	// TODO
	public void updateCalendarEvent(long ceid, CalendarEventDto dto, String userEmail) {
		// edit
		// the dto is currently a placeholder
		// must verify user is the event owner
	}
	
	// TODO
	public void deleteCalendarEvent(long ceid, String userEmail) {
		// delete
		// must verify user is the event owner
	}
	
	public List<CalendarEvent> getAllEventsOfAccount(
			long userId,
			boolean sameUser
	) {
		return this.calendarEventRepository.findAllByAccountId(
				userId, sameUser);
	}
	
	// TODO
	public List<CalendarEvent> getAllEventsAfterDate(
			long userId,
			boolean isUser,
			LocalDateTime originDate
	) {
		return this.calendarEventRepository.findAllOfAccountAfterDate(
				userId, isUser, originDate);
	}
	
	// TODO
	public List<CalendarEvent> getAllEventsBetweenDates(
			long userId,
			boolean isUser,
			LocalDateTime startDate,
			LocalDateTime endDate
	) {
		return this.calendarEventRepository.findAllOfAccountBetweenDates(
				userId, isUser, startDate, endDate);
	}
}
