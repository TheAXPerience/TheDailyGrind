package com.alvinxu.TheDailyGrind.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DiaryEntryDto {
	@NotBlank(message="Diary Entry must be titled.")
	@Size(min=1, max=255, message="Title must contain 1-255 characters.")
	private String title;
	
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
	
	
}
