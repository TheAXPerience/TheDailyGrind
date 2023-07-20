package com.alvinxu.TheDailyGrind.models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CalendarEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // primary key
	
	@Column
	private Account user; // foreign key
	
	@Column(columnDefinition="varchar(255)")
	private String title;
	
	// TODO: size?
	@Column(columnDefinition="varchar(1000)")
	private String description;
	
	@Column
	private Date datetime; // stored in UTC; front-end must do conversion
}
