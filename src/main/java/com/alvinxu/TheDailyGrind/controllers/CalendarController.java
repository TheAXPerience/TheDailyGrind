package com.alvinxu.TheDailyGrind.controllers;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.alvinxu.TheDailyGrind.dto.CalendarEventDto;
import com.alvinxu.TheDailyGrind.dto.DiaryEntryDto;
import com.alvinxu.TheDailyGrind.dto.MonthYearDto;
import com.alvinxu.TheDailyGrind.dto.SearchQueryDto;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.models.CalendarEvent;
import com.alvinxu.TheDailyGrind.models.DiaryEntry;
import com.alvinxu.TheDailyGrind.services.AccountService;
import com.alvinxu.TheDailyGrind.services.CalendarEventService;
import com.alvinxu.TheDailyGrind.services.DiaryEntryService;
import com.alvinxu.TheDailyGrind.validators.PasswordValidator;

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
		
		SearchQueryDto searchQueryDto = new SearchQueryDto();
		model.addAttribute("searchQueryDto", searchQueryDto);
		
		MonthYearDto monthYearDto = new MonthYearDto();
		model.addAttribute("monthYearDto", monthYearDto);
		
		/*
		model.addAttribute("events_list",
					this.calendarEventService.getAllEventsOfAccount(user.getId(), true));
		
		model.addAttribute("events_list_nonuser",
				   this.calendarEventService.getAllEventsOfAccount(user.getId(), false));
		*/
		
		model.addAttribute("events_list_upcoming",
		    this.calendarEventService.getAllEventsAfterDate(
		        user.getId(),
		        true,
		        LocalDateTime.now()
		    )
		);
		
		YearMonth yearMonth = YearMonth.now();
		model.addAttribute("events_list_between",
		    this.calendarEventService.getAllEventsBetweenDates(
		        user.getId(),
		        true,
		        yearMonth.atDay(1).atStartOfDay(),
		        yearMonth.plusMonths(1).atDay(1).atStartOfDay().minusMinutes(1)
        )
		);
		
		/*
		model.addAttribute("entry_list",
				   this.diaryEntryService.getAllEventsOfAccount(
						   user.getId()
				   )
		); */
		
		model.addAttribute("entry_list_recent",
        this.diaryEntryService.getAllEntriesBeforeDate(
            user.getId(),
            LocalDateTime.now()
        )
    );
		
		model.addAttribute("entry_list_between",
        this.diaryEntryService.getAllEntriesBetweenDates(
            user.getId(),
            yearMonth.atDay(1).atStartOfDay(),
            yearMonth.plusMonths(1).atDay(1).atStartOfDay()
        )
		);
		
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
		CalendarEventDto dto = new CalendarEventDto();
		try {
		  CalendarEvent cevent = this.calendarEventService.getById(ceid);
			dto.setDateOfEvent(cevent.getDateOfEvent());
			dto.setDescription(cevent.getDescription());
			dto.setTitle(cevent.getTitle());
			dto.setCompleteToggle(cevent.isComplete());
			dto.setPublicToggle(cevent.isPublic());
			
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
		model.addAttribute("formHeading", "New Diary Entry");
    model.addAttribute("submitLink", "/new-diary-entry/");
		return "diary-entry-form";
	}
	
	@PostMapping("/new-diary-entry/")
	public String diaryEntrySubmit(Model model,
				@Valid DiaryEntryDto diaryEntryDto,
				BindingResult bindingResult,
				Principal principal) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("diaryEntryDto", diaryEntryDto);
			model.addAttribute("formHeading", "New Diary Entry");
      model.addAttribute("submitLink", "/new-diary-entry/");
			return "diary-entry-form";
		}
		
		// System.out.println(calendarEventDto);
		
		try {
		  diaryEntryService.saveNewDiaryEntry(diaryEntryDto, principal.getName());
		} catch (Exception e) {
			bindingResult.reject(e.getMessage());
			model.addAttribute("diaryEntryDto", diaryEntryDto);
			model.addAttribute("formHeading", "New Diary Entry");
			model.addAttribute("submitLink", "/new-diary-entry/");
			return "diary-entry-form";
		}
		
		return "redirect:/home";
	}
	
	@GetMapping("/edit-diary-entry/{deid}")
	public String editDiaryEntry(Model model,
	    @PathVariable("deid") Long deid,
	    Principal principal
	  ) {
	  DiaryEntryDto dto = new DiaryEntryDto();
	  try {
	    DiaryEntry dentry = this.diaryEntryService.getById(deid);
	    dto.setDate_of_entry(dentry.getDateOfEntry().toLocalDate());
	    dto.setTitle(dentry.getTitle());
	    dto.setEntry(dentry.getEntry());
	    
	    if (!dentry.getDiaryOwner().getEmail().equals(principal.getName())) {
        // i have no idea what to do here
        return "redirect:/home";
      }
	  } catch (Exception e) {
	    // ???
	    return "redirect:/home";
	  }
	  model.addAttribute("diaryEntryDto", dto);
	  model.addAttribute("method", "PUT");
	  model.addAttribute("formHeading", "Edit Diary Entry #" + deid);
	  model.addAttribute("submitLink", "/edit-diary-entry/" + deid + "/");
	  return "diary-entry-form";
	}
	
	@PutMapping("/edit-diary-entry/{deid}/")
	public String editDiaryEntrySubmit(Model model,
	    @PathVariable("deid") Long deid,
	    DiaryEntryDto diaryEntryDto,
	    BindingResult bindingResult,
	    Principal principal
	  ) {
	  if (bindingResult.hasErrors()) {
	    model.addAttribute("diaryEntryDto", diaryEntryDto);
	    model.addAttribute("method", "PUT");
	    model.addAttribute("formHeading", "Edit Diary Entry #" + deid);
	    model.addAttribute("submitLink", "/edit-diary-entry/" + deid + "/");
      return "calendar-event-form";
    }
    
    try {
      System.out.println(diaryEntryDto.getDate_of_entry());
      diaryEntryService.updateDiaryEntry(
          deid, principal.getName(), diaryEntryDto);
    } catch (Exception e) {
      bindingResult.reject(e.getMessage());
      model.addAttribute("diaryEntryDto", diaryEntryDto);
      model.addAttribute("method", "PUT");
      model.addAttribute("formHeading", "Edit Diary Entry #" + deid);
      model.addAttribute("submitLink", "/edit-diary-entry/" + deid + "/");
      return "calendar-event-form";
    }
    
    return "redirect:/home";
	}
	
	@DeleteMapping("/delete-diary-entry/{deid}/")
	public String deleteDiaryEntry(
	    @PathVariable("deid") Long deid,
	    Principal principal
	  ) {
	  try {
      this.diaryEntryService.deleteDiaryEntry(deid, principal.getName());
    } catch (Exception e) {
      // i have no idea how to handle this
      return "redirect:/home";
    }
    
    return "redirect:/home";
	}

	@GetMapping("/search")
	public String searchForUser(Model model,
	    @Valid SearchQueryDto searchQueryDto,
	    BindingResult bindingResult
	) {
	  model.addAttribute("searchQueryDto", searchQueryDto);
	  if (bindingResult.hasErrors()) {
	    model.addAttribute("searchResults", new ArrayList<Account>());
	  } else {
	    List<Account> searchResults = this.accountService.getAccountsLikeUsername(searchQueryDto.getQuery());
	    model.addAttribute("searchResults", searchResults);
	  }
	  
	  return "search-username";
	}
	
	@GetMapping("/month-year")
	public String searchForMonthYear(Model model,
	    @Valid MonthYearDto monthYearDto,
	    BindingResult bindingResult,
	    Principal principal
	) {
	  Account user = this.accountService.getAccountByEmail(principal.getName());
	  model.addAttribute("user", user);
	  model.addAttribute("showEditDelete", true);
	  
	  if (bindingResult.hasErrors()) {
	    model.addAttribute("heading", "Error Parsing Month and Year");
	    model.addAttribute("eventsList", new ArrayList<CalendarEvent>());
	    return "month-year-calendar";
	  }
	  
	  List<CalendarEvent> eventsList = this.calendarEventService.getAllEventsBetweenDates(
	      user.getId(),
	      false,
	      monthYearDto.getMonthYear().atDay(1).atStartOfDay(),
	      monthYearDto.getMonthYear().plusMonths(1).atDay(1).atStartOfDay().minusMinutes(1)
	  );
	  
	  List<DiaryEntry> entriesList = this.diaryEntryService.getAllEntriesBetweenDates(
	      user.getId(),
	      monthYearDto.getMonthYear().atDay(1).atStartOfDay(),
	      monthYearDto.getMonthYear().plusMonths(1).atDay(1).atStartOfDay()
	  );
	  
	  model.addAttribute("eventsList", eventsList);
	  model.addAttribute("entriesList", entriesList);
	  model.addAttribute("heading",
	      monthYearDto
	      .getMonthYear()
	      .format(DateTimeFormatter.ofPattern("MMMM yyyy"))
	  );
	  
	  return "month-year-calendar";
	}
	
	@GetMapping("/user/{userId}")
	public String userPage(Model model,
	    @PathVariable("userId") Long userId
	) {
	  Account user;
	  try {
	    user = this.accountService.getAccountById(userId);
	  } catch (Exception e) {
	    // ???
	    return "redirect:/home";
	  }
	  
	  model.addAttribute("user", user);
	  model.addAttribute("searchQueryDto", new SearchQueryDto());
	  model.addAttribute("monthYearDto", new MonthYearDto());
	  
	  model.addAttribute("eventsList_upcoming",
	      this.calendarEventService.getAllEventsAfterDate(
	          userId, false, LocalDateTime.now()
	      )
	  );
	  
	  model.addAttribute("eventsList_thisMonth",
	      this.calendarEventService.getAllEventsBetweenDates(
	          userId,
	          false,
	          YearMonth.now().atDay(1).atStartOfDay(),
	          YearMonth.now().plusMonths(1).atDay(1).atStartOfDay().minusMinutes(1)
	      )
	  );
	  
	  return "user-home";
	}
	
	@GetMapping("/user/{userId}/month-year")
	public String userPageByMonth(Model model,
	    @PathVariable("userId") Long userId,
	    @Valid MonthYearDto monthYearDto,
	    BindingResult bindingResult
	) {
	  try {
	    Account user = this.accountService.getAccountById(userId);
	    model.addAttribute("user", user);
	    model.addAttribute("heading",
	        user.getUsername() + ": " + monthYearDto
	        .getMonthYear()
	        .format(DateTimeFormatter.ofPattern("MMMM yyyy"))
	    );
	  } catch (Exception e) {
	    // ???
	    return "redirect:/home";
	  }
	  model.addAttribute("showEditDelete", false);
    
    if (bindingResult.hasErrors()) {
      model.addAttribute("heading", "Error Parsing Month and Year");
      model.addAttribute("eventsList", new ArrayList<CalendarEvent>());
      return "month-year-calendar";
    }
    
    List<CalendarEvent> eventsList = this.calendarEventService.getAllEventsBetweenDates(
        userId,
        false,
        monthYearDto.getMonthYear().atDay(1).atStartOfDay(),
        monthYearDto.getMonthYear().plusMonths(1).atDay(1).atStartOfDay().minusMinutes(1)
    );
    
    model.addAttribute("eventsList", eventsList);
    
    return "month-year-calendar";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
	  // add trimming support for input fields
	  webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	  
	  // TODO: add validator for password field
	  webDataBinder.addValidators(new PasswordValidator());
	}
}
