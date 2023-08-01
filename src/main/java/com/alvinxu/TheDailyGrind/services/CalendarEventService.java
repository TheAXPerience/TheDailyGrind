package com.alvinxu.TheDailyGrind.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		cevent.setDateOfEvent(dto.getDateOfEvent());
		cevent.setTitle(dto.getTitle());
		cevent.setDescription(dto.getDescription());
		cevent.setPublic(dto.getPublicToggle());
		cevent.setComplete(dto.getCompleteToggle());
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
		cevent.setDateOfEvent(edited.getDateOfEvent());
		cevent.setTitle(edited.getTitle());
		cevent.setDescription(edited.getDescription());
		cevent.setPublic(edited.getPublicToggle());
		cevent.setComplete(edited.getCompleteToggle());
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
	
	public Page<CalendarEvent> getAllEventsOfAccount(
			long userId,
			boolean sameUser,
			int page,
			int size
	) {
	  Pageable pageable = PageRequest.of(page, size);
		return this.calendarEventRepository.findAllByAccountId(
				userId, sameUser, pageable);
	}
	
	public Page<CalendarEvent> getAllEventsAfterDate(
			long userId,
			boolean isUser,
			LocalDateTime originDate,
			int page,
			int size
	) {
	  Pageable pageable = PageRequest.of(page, size);
		return this.calendarEventRepository.findAllOfAccountAfterDate(
				userId, isUser, originDate, pageable);
	}
	
	public Page<CalendarEvent> getAllEventsBetweenDates(
			long userId,
			boolean isUser,
			LocalDateTime startDate,
			LocalDateTime endDate,
			int page,
			int size
	) {
	  Pageable pageable = PageRequest.of(page, size);
		return this.calendarEventRepository.findAllOfAccountBetweenDates(
				userId, isUser, startDate, endDate, pageable);
	}
}
