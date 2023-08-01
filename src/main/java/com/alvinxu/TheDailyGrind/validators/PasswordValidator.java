package com.alvinxu.TheDailyGrind.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidatorConstraint, char[]> {

  @Override
  public boolean isValid(char[] value, ConstraintValidatorContext context) {
    int lower = 0, upper = 0, digits = 0;
    for (char c : value) {
      if (c >= 'a' && c <= 'z') {
        lower++;
      } else if (c >= 'A' && c <= 'Z') {
        upper++;
      } else if (c >= '0' && c <= '9') {
        digits++;
      }
    }
    return lower > 0 && upper > 0 && digits > 0;
  }

}
