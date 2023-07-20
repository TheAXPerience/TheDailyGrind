package com.alvinxu.TheDailyGrind.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {
	@NotEmpty
	@Email
	private String email;
	
	@Size(min=3, max=255)
	private String username;
	
	@Size(min=8, max=255)
	private String password;
	
	@NotEmpty
	private java.util.Date date_of_birth;

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
}
