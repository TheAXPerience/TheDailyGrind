package com.alvinxu.TheDailyGrind.dto;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CalendarEventDto {
	@NotNull(message="Date must not be null.")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private java.util.Date date_of_event;
	
	@Size(min=1, max=200, message="Event Name must be between 1-200 characters.")
	private String event_name;
	
	private String description;
	
	private boolean is_public;

	public java.util.Date getDate_of_event() {
		return date_of_event;
	}

	public void setDate_of_event(java.util.Date date_of_event) {
		this.date_of_event = date_of_event;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIs_public() {
		return is_public;
	}

	public void setIs_public(boolean is_public) {
		this.is_public = is_public;
	}

	@Override
	public String toString() {
		return "CalendarEventDto [date_of_event=" + date_of_event + ", event_name=" + event_name + ", description="
				+ description + ", is_public=" + is_public + "]";
	}
}
