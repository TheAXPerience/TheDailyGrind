package com.alvinxu.TheDailyGrind.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class DiaryEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // primary key
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Account diaryOwner; // foreign key
	
	@CreationTimestamp
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateCreated; // date created
	// date created also acts as the date of entry (in UTC)
	
	@UpdateTimestamp
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateUpdated; // date last edited
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateOfEntry;
	
	@Column(columnDefinition="VARCHAR(255) not null")
	private String title;
	
	@Column(columnDefinition="TEXT")
	private String entry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getDiaryOwner() {
		return diaryOwner;
	}

	public void setDiaryOwner(Account diaryOwner) {
		this.diaryOwner = diaryOwner;
	}

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

  public LocalDateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public LocalDateTime getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(LocalDateTime dateUpdated) {
    this.dateUpdated = dateUpdated;
  }

  public LocalDateTime getDateOfEntry() {
    return dateOfEntry;
  }

  public void setDateOfEntry(LocalDateTime dateOfEntry) {
    this.dateOfEntry = dateOfEntry;
  }
	
}
