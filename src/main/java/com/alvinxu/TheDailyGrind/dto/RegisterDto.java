package com.alvinxu.TheDailyGrind.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.alvinxu.TheDailyGrind.validators.EmailValidatorConstraint;
import com.alvinxu.TheDailyGrind.validators.PasswordValidatorConstraint;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class RegisterDto {
	@NotEmpty(message="{email.not.empty}")
	@Email(message="{email.not.valid}")
	@EmailValidatorConstraint(message="{email.already.exists}")
	private String email;
	
	@Size(min=3, max=255, message="{username.size}")
	private String username;
	
	@PasswordValidatorConstraint(message="{password.not.valid}")
	@Size(min=8, max=255, message="{password.size}")
	private char[] password;
	
	@Past(message="{date.of.birth.future}")
	@NotNull(message="{date.of.birth.not.null}")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth = LocalDate.now();
	
	@AssertTrue(message="{terms.of.service}")
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

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public boolean isTos_agree() {
		return tos_agree;
	}

	public void setTos_agree(boolean tos_agree) {
		this.tos_agree = tos_agree;
	}
	
}
