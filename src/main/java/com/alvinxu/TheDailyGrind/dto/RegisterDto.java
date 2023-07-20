package com.alvinxu.TheDailyGrind.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.alvinxu.TheDailyGrind.validators.DateFormatConstraint;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {
	@NotEmpty(message="Must submit email to create account.")
	@Email
	private String email;
	
	@Size(min=3, max=255, message="Username must be at least 3 characters long")
	private String username;
	
	@Size(min=8, max=255, message="Password must be at least 8 characters long")
	private String password;
	
	@DateFormatConstraint(message="Invalid date format submitted.")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date date_of_birth;
	
	@AssertTrue(message="Must agree to the Terms of Service")
	private boolean tos_agree;

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

	public java.util.Date getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(java.util.Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public boolean isTos_agree() {
		return tos_agree;
	}

	public void setTos_agree(boolean tos_agree) {
		this.tos_agree = tos_agree;
	}
	
}
