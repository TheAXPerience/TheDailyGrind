package com.alvinxu.TheDailyGrind.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CalendarEventDto {
	@NotNull(message="{date.time.not.null}")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime dateOfEvent = LocalDateTime.now();
	
	@Size(min=1, max=200, message="{title.length}")
	private String title;
	
	private String description;
	
	private boolean publicToggle = false;
	
	private boolean completeToggle = false;
	
	public LocalDateTime getDateOfEvent() {
    return dateOfEvent;
  }

  public void setDateOfEvent(LocalDateTime dateOfEvent) {
    this.dateOfEvent = dateOfEvent;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

  public boolean getPublicToggle() {
    return publicToggle;
  }

  public void setPublicToggle(boolean publicToggle) {
    this.publicToggle = publicToggle;
  }

  public boolean getCompleteToggle() {
    return completeToggle;
  }

  public void setCompleteToggle(boolean completeToggle) {
    this.completeToggle = completeToggle;
  }
	
}
