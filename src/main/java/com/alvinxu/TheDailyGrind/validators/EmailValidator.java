package com.alvinxu.TheDailyGrind.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.alvinxu.TheDailyGrind.services.AccountService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailValidatorConstraint, String> {

  @Autowired
  AccountService accountService;
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // TODO Auto-generated method stub
    return this.accountService.getAccountByEmail(value) == null;
  }

}
