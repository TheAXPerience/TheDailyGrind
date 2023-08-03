package com.alvinxu.TheDailyGrind.models;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Account {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true)
	private String email;
	
	@Column
	private String username;
	
	@Column
	private String password;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateOfBirth;
	
	/*
	 * USER or ADMIN
	 */
	@Column(columnDefinition = "varchar(8) default 'USER'")
	private String authority = "USER";
	
	public Account() {}

	public Account(Long id, String email, String username, String password, LocalDateTime dateOfBirth, String authority) {
    super();
    this.id = id;
    this.email = email;
    this.username = username;
    this.password = password;
    this.dateOfBirth = dateOfBirth;
    this.authority = authority;
  }

  public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

  public LocalDateTime getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDateTime dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(authority, dateOfBirth, email, id, password, username);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Account other = (Account) obj;
    return Objects.equals(authority, other.authority) && Objects.equals(dateOfBirth, other.dateOfBirth)
        && Objects.equals(email, other.email) && Objects.equals(id, other.id)
        && Objects.equals(password, other.password) && Objects.equals(username, other.username);
  }
	
}
