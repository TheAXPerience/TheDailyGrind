package com.alvinxu.TheDailyGrind.models;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

// @Entity
public class CalendarEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // primary key
	
	@Column
	private Account user; // foreign key
	
	@CreationTimestamp
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_created; // date created
	// date created also acts as the date of entry (in UTC)
	
	@UpdateTimestamp
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date date_updated; // date last edited
	
	@Column
	private Date date_of_event; // stored in UTC; front-end must do conversion
	
	@Column(columnDefinition="varchar(255)")
	private String title;
	
	// TODO: size?
	@Column(columnDefinition="varchar(1000)")
	private String description;
	
	@Column
	private boolean is_complete = false;
}
