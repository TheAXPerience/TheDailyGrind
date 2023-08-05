package com.alvinxu.TheDailyGrind.validators;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPasswordValidator {
  private PasswordValidator validator;
  
  @BeforeEach
  void setup() {
    validator = new PasswordValidator();
  }
  
  @Test
  void testIsValid() {
    char[] password = {'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1'};
    
    boolean expected = validator.isValid(password, null);
    assertThat(expected).isTrue();
  }
  
  @Test
  void testIsInvalidNoNumber() {
    char[] password = {'P', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    
    boolean expected = validator.isValid(password, null);
    assertThat(expected).isFalse();
  }
  
  @Test
  void testIsInvalidNoUppercase() {
    char[] password = {'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1'};
    
    boolean expected = validator.isValid(password, null);
    assertThat(expected).isFalse();
  }
  
  @Test
  void testIsInvalidNoLowercase() {
    char[] password = {'P', 'A', 'S', 'S', 'W', 'O', 'R', 'D', '1'};
    
    boolean expected = validator.isValid(password, null);
    assertThat(expected).isFalse();
  }
}
