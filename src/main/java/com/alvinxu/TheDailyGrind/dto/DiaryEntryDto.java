package com.alvinxu.TheDailyGrind.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DiaryEntryDto {
	@Size(min=1, max=200, message="{title.length}")
	private String title;
	
	@NotNull(message="{date.not.null}")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
	private LocalDate date_of_entry = LocalDate.now();
	
	@NotBlank(message = "{entry.empty}")
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

	public LocalDate getDate_of_entry() {
		return date_of_entry;
	}

	public void setDate_of_entry(LocalDate date_of_entry) {
		this.date_of_entry = date_of_entry;
	}
	
	
}
