package com.alvinxu.TheDailyGrind.validators;

import com.alvinxu.TheDailyGrind.services.AccountService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailValidatorConstraint, String> {
  private AccountService accountService;
  
  public EmailValidator(AccountService accountService) {
    this.accountService = accountService;
  }
  
  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return this.accountService.getAccountByEmail(value) == null;
  }

}
