package com.alvinxu.TheDailyGrind.models;

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
	@Temporal(TemporalType.DATE)
	private java.util.Date date_of_birth;
	
	/*
	 * USER or ADMIN
	 */
	@Column(columnDefinition = "varchar(8) default 'USER'")
	private String authority = "USER";

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

	public java.util.Date getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(java.util.Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
}
