package com.alvinxu.TheDailyGrind.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alvinxu.TheDailyGrind.dto.CalendarEventDto;
import com.alvinxu.TheDailyGrind.exceptions.EventEntryNotFoundException;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.CalendarEvent;
import com.alvinxu.TheDailyGrind.repositories.CalendarEventRepository;

import jakarta.transaction.Transactional;

@Service
public class CalendarEventService {
	private CalendarEventRepository calendarEventRepository;
	private AccountService accountService;
	
	public CalendarEventService(AccountService accountService, CalendarEventRepository calendarEventRepository) {
	  this.accountService = accountService;
	  this.calendarEventRepository = calendarEventRepository;
	}
	
	public CalendarEvent getById(long ceid) {
		Optional<CalendarEvent> cevent = this.calendarEventRepository.findById(ceid);
		if (cevent.isEmpty()) {
			throw new EventEntryNotFoundException("Error: event does not exist.");
		}
		return cevent.get();
	}
	
	@Transactional
	public void saveNewCalendarEvent(String userEmail, CalendarEventDto dto) {
		Account user = accountService.getAccountByEmail(userEmail);
		if (user == null) {
			throw new UsernameNotFoundException("Error: could not verify user.");
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
	public void updateCalendarEvent(long ceid, String userEmail, CalendarEventDto edited)
	    throws EventEntryNotFoundException, IllegalAccessException {
		Optional<CalendarEvent> calvent = this.calendarEventRepository.findById(ceid);
		if (calvent.isEmpty()) {
			// placeholder exception: could not find event
			throw new EventEntryNotFoundException("Error: event does not exist.");
		}
		
		CalendarEvent cevent = calvent.get();
		if (!cevent.getEventOrganizer().getEmail().equals(userEmail)) {
			// placeholder exception: user trying to edit an event they do not own
			throw new IllegalAccessException("Error: user does not have permission to edit event.");
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
	public void deleteCalendarEvent(long ceid, String userEmail) throws EventEntryNotFoundException, IllegalAccessException {
	  Optional<CalendarEvent> calvent = this.calendarEventRepository.findById(ceid);
    if (calvent.isEmpty()) {
      // placeholder exception: could not find event
      throw new EventEntryNotFoundException("Error: event does not exist.");
    }
    
    CalendarEvent cevent = calvent.get();
    if (!cevent.getEventOrganizer().getEmail().equals(userEmail)) {
      // placeholder exception: user trying to edit an event they do not own
      throw new IllegalAccessException("Error: user does not have permission to delete event.");
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
