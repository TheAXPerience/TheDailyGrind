package com.alvinxu.TheDailyGrind.models;

import java.time.LocalDateTime;
import java.util.Objects;

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
// @Table(name="CalendarEvent")
public class CalendarEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // primary key
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Account eventOrganizer; // foreign key
	
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
	private LocalDateTime dateOfEvent; // stored in UTC; front-end must do conversion
	
	@Column(columnDefinition="VARCHAR(255)")
	private String title;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	@Column
	private boolean isComplete = false;
	
	@Column
	private boolean isPublic = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getEventOrganizer() {
		return eventOrganizer;
	}

	public void setEventOrganizer(Account eventOrganizer) {
		this.eventOrganizer = eventOrganizer;
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

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

  @Override
  public int hashCode() {
    return Objects.hash(dateCreated, dateOfEvent, dateUpdated, description, eventOrganizer, id, isComplete, isPublic,
        title);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CalendarEvent other = (CalendarEvent) obj;
    return Objects.equals(dateCreated, other.dateCreated) && Objects.equals(dateOfEvent, other.dateOfEvent)
        && Objects.equals(dateUpdated, other.dateUpdated) && Objects.equals(description, other.description)
        && Objects.equals(eventOrganizer, other.eventOrganizer) && Objects.equals(id, other.id)
        && isComplete == other.isComplete && isPublic == other.isPublic && Objects.equals(title, other.title);
  }
	
	
}
