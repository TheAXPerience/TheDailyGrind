package com.alvinxu.TheDailyGrind.controllers;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.alvinxu.TheDailyGrind.dto.CalendarEventDto;
import com.alvinxu.TheDailyGrind.dto.DiaryEntryDto;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.CalendarEvent;
import com.alvinxu.TheDailyGrind.services.AccountService;
import com.alvinxu.TheDailyGrind.services.CalendarEventService;
import com.alvinxu.TheDailyGrind.services.DiaryEntryService;

import jakarta.validation.Valid;

@Controller
public class CalendarController {
	@Autowired
	AccountService accountService;
	
	@Autowired
	CalendarEventService calendarEventService;
	
	@Autowired
	DiaryEntryService diaryEntryService;
	
	@GetMapping("/home")
	public String home(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}
		Account user = accountService.getAccountByEmail(principal.getName());
		model.addAttribute("user", user);
		
		model.addAttribute("events_list",
					this.calendarEventService.getAllEventsOfAccount(user.getId(), true));
		
		model.addAttribute("events_list_nonuser",
				   this.calendarEventService.getAllEventsOfAccount(user.getId(), false));
		
		model.addAttribute("events_list_upcoming",
				   this.calendarEventService.getAllEventsAfterDate(
						   user.getId(),
						   true,
						   LocalDateTime.now()
		));
		
		YearMonth yearMonth = YearMonth.now();
		model.addAttribute("events_list_between",
				   this.calendarEventService.getAllEventsBetweenDates(
						   user.getId(),
						   true,
						   yearMonth.atDay(1).atStartOfDay(),
						   yearMonth.plusMonths(1).atDay(1).atStartOfDay().minusMinutes(1)
		));
		
		model.addAttribute("entry_list",
				   this.diaryEntryService.getAllEventsOfAccount(
						   user
		));
		
		return "home";
	}
	
	@GetMapping("/new-calendar-event")
	public String calendarEventForm(Model model) {
		model.addAttribute("calendarEventDto", new CalendarEventDto());
		model.addAttribute("formHeading", "Create New Event");
		model.addAttribute("submitLink", "/new-calendar-event/");
		return "calendar-event-form";
	}
	
	@PostMapping("/new-calendar-event/")
	public String calendarEventSubmit(Model model,
				@Valid CalendarEventDto calendarEventDto,
				BindingResult bindingResult,
				Principal principal) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("calendarEventDto", calendarEventDto);
			model.addAttribute("formHeading", "Create New Event");
			model.addAttribute("submitLink", "/new-calendar-event/");
			return "calendar-event-form";
		}
		
		// System.out.println(calendarEventDto);
		try {
			calendarEventService.saveNewCalendarEvent(
					principal.getName(), calendarEventDto
			);
		} catch (Exception e) {
			bindingResult.reject(e.getMessage());
			model.addAttribute("calendarEventDto", calendarEventDto);
			model.addAttribute("formHeading", "Create New Event");
			model.addAttribute("submitLink", "/new-calendar-event/");
			return "calendar-event-form";
		}
		
		return "redirect:/home";
	}
	
	@GetMapping("/edit-calendar-event/{ceid}")
	public String editCalendarEvent(Model model,
			@PathVariable("ceid") long ceid,
			Principal principal
	) {
		CalendarEvent cevent;
		CalendarEventDto dto = new CalendarEventDto();
		try {
			cevent = this.calendarEventService.getById(ceid);
			dto.setDate_of_event(cevent.getDateOfEvent());
			dto.setDescription(cevent.getDescription());
			dto.setEvent_name(cevent.getTitle());
			dto.setIs_complete(cevent.isComplete());
			dto.setIs_public(cevent.isPublic());
			
			if (!cevent.getEventOrganizer().getEmail().equals(principal.getName())) {
			  // i have no idea what to do here
				return "redirect:/home";
			}
		} catch (Exception e) {
			return "redirect:/home";
		}
		
		model.addAttribute("calendarEventDto", dto);
		model.addAttribute("method", "PUT");
		model.addAttribute("formHeading", "Edit Event #" + ceid);
		model.addAttribute("submitLink", "/edit-calendar-event/" + ceid + "/");
		return "calendar-event-form";
	}
	
	@PutMapping("/edit-calendar-event/{ceid}/")
	public String editCalendarEventSubmit(Model model,
			@PathVariable("ceid") Long ceid,
			@Valid CalendarEventDto calendarEventDto,
			BindingResult bindingResult,
			Principal principal
	) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("calendarEventDto", calendarEventDto);
			model.addAttribute("method", "PUT");
			model.addAttribute("formHeading", "Edit Event #" + ceid);
			model.addAttribute("submitLink", "/edit-calendar-event/" + ceid + "/");
			return "calendar-event-form";
		}
		
		try {
			calendarEventService.updateCalendarEvent(
					ceid, principal.getName(), calendarEventDto);
		} catch (Exception e) {
			bindingResult.reject(e.getMessage());
			model.addAttribute("calendarEventDto", calendarEventDto);
			model.addAttribute("method", "PUT");
			model.addAttribute("formHeading", "Edit Event #" + ceid);
			model.addAttribute("submitLink", "/edit-calendar-event/" + ceid + "/");
			return "calendar-event-form";
		}
		
		return "redirect:/home";
	}
	
	@DeleteMapping("/delete-calendar-event/{ceid}/")
	public String deleteCalendarEvent(
	    @PathVariable("ceid") Long ceid,
	    Principal principal
	) {
	  try {
	    this.calendarEventService.deleteCalendarEvent(ceid, principal.getName());
	  } catch (Exception e) {
	    // i have no idea how to handle this
	    return "redirect:/home";
	  }
	  
	  return "redirect:/home";
	}
	
	@GetMapping("/new-diary-entry")
	public String diaryEntryForm(Model model) {
		model.addAttribute("diaryEntryDto", new DiaryEntryDto());
		return "diary-entry-form";
	}
	
	@PostMapping("/new-diary-entry/")
	public String diaryEntrySubmit(Model model,
				@Valid DiaryEntryDto diaryEntryDto,
				BindingResult bindingResult,
				Principal principal) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("diaryEntryDto", diaryEntryDto);
			return "diary-entry-form";
		}
		
		// System.out.println(calendarEventDto);
		
		if (!diaryEntryService.saveNewDiaryEntry(diaryEntryDto, principal.getName())) {
			bindingResult.reject("An error occurred registering your entry.");
			model.addAttribute("diaryEntryDto", diaryEntryDto);
			return "diary-entry-form";
		}
		
		return "redirect:/home";
	}
}
