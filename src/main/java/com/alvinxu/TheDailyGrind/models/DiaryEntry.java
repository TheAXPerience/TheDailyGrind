package com.alvinxu.TheDailyGrind.models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DiaryEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // primary key
	
	@Column
	private Account user; // foreign key
	
	@Column(columnDefinition="varchar(255) not null")
	private String title;
	
	// TODO: size?
	@Column(columnDefinition="varchar(10000)")
	private String entry;
	
	@Column
	private Date date; // date of entry
}
