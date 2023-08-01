package com.alvinxu.TheDailyGrind.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.alvinxu.TheDailyGrind.dto.RegisterDto;

public class PasswordValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    // TODO Auto-generated method stub
    return RegisterDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // TODO Auto-generated method stub
    RegisterDto dto = (RegisterDto) target;
    
    if (!validatePassword(dto.getPassword())) {
      errors.rejectValue("password", "password.not.valid");
    }
  }
  
  private boolean validatePassword(char[] password) {
    int lower = 0, upper = 0, digits = 0;
    for (char c : password) {
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
