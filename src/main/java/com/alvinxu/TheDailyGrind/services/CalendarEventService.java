package com.alvinxu.TheDailyGrind.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
	
	public CalendarEvent getById(long ceid) {
		Optional<CalendarEvent> cevent = this.calendarEventRepository.findById(ceid);
		if (cevent.isEmpty()) {
			throw new IllegalArgumentException("Error: event does not exist");
		}
		return cevent.get();
	}
	
	@Transactional
	public void saveNewCalendarEvent(String userEmail, CalendarEventDto dto) {
		Account user = accountService.getAccountByEmail(userEmail);
		if (user == null) {
			throw new IllegalArgumentException("Error: could not verify user");
		}
		
		CalendarEvent cevent = new CalendarEvent();
		cevent.setEventOrganizer(user);
		cevent.setDateOfEvent(dto.getDate_of_event());
		cevent.setTitle(dto.getEvent_name());
		cevent.setDescription(dto.getDescription());
		cevent.setPublic(dto.isIs_public());
		cevent.setComplete(dto.isIs_complete());
		this.calendarEventRepository.save(cevent);
	}
	
	@Transactional
	public void updateCalendarEvent(long ceid, String userEmail, CalendarEventDto edited) {
		Optional<CalendarEvent> calvent = this.calendarEventRepository.findById(ceid);
		if (calvent.isEmpty()) {
			// placeholder exception: could not find event
			throw new IllegalArgumentException("Error: could not find event");
		}
		
		CalendarEvent cevent = calvent.get();
		if (!cevent.getEventOrganizer().getEmail().equals(userEmail)) {
			// placeholder exception: user trying to edit an event they do not own
			throw new IllegalArgumentException("Error: does not have permission to edit event");
		}
		
		// update event using information from DTO
		cevent.setDateOfEvent(edited.getDate_of_event());
		cevent.setTitle(edited.getEvent_name());
		cevent.setDescription(edited.getDescription());
		cevent.setPublic(edited.isIs_public());
		cevent.setComplete(edited.isIs_complete());
		this.calendarEventRepository.save(cevent);
	}
	
	@Transactional
	public void deleteCalendarEvent(long ceid, String userEmail) {
	  Optional<CalendarEvent> calvent = this.calendarEventRepository.findById(ceid);
    if (calvent.isEmpty()) {
      // placeholder exception: could not find event
      throw new IllegalArgumentException("Error: could not find event");
    }
    
    CalendarEvent cevent = calvent.get();
    if (!cevent.getEventOrganizer().getEmail().equals(userEmail)) {
      // placeholder exception: user trying to edit an event they do not own
      throw new IllegalArgumentException("Error: does not have permission to delete event");
    }
    
    this.calendarEventRepository.delete(cevent);
	}
	
	public List<CalendarEvent> getAllEventsOfAccount(
			long userId,
			boolean sameUser
	) {
		return this.calendarEventRepository.findAllByAccountId(
				userId, sameUser);
	}
	
	public List<CalendarEvent> getAllEventsAfterDate(
			long userId,
			boolean isUser,
			LocalDateTime originDate
	) {
		return this.calendarEventRepository.findAllOfAccountAfterDate(
				userId, isUser, originDate);
	}
	
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
