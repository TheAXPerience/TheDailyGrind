package com.alvinxu.TheDailyGrind.dto;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DiaryEntryDto {
	@NotBlank(message="Diary Entry must be titled.")
	@Size(min=1, max=255, message="Title must contain 1-255 characters.")
	private String title;
	
	@NotNull(message="Date must not be null.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date date_of_entry;
	
	@NotBlank(message = "Diary Entry cannot be blank.")
	private String entry;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public java.util.Date getDate_of_entry() {
		return date_of_entry;
	}

	public void setDate_of_entry(java.util.Date date_of_entry) {
		this.date_of_entry = date_of_entry;
	}
	
	
}
