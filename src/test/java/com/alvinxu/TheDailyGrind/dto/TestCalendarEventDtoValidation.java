package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@TestInstance(Lifecycle.PER_CLASS)
public class TestCalendarEventDtoValidation {
  private CalendarEventDto dto;
  
  private ValidatorFactory factory;
  private Validator validator;
  
  @BeforeAll
  void factorySetup() {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }
  
  @AfterAll
  void factoryTeardown() {
    factory.close();
  }
  
  @BeforeEach
  void setup() {
    dto = new CalendarEventDto();
    dto.setTitle("my title");
    dto.setDescription("my description");
    dto.setDateOfEvent(LocalDateTime.of(2023, 9, 30, 5, 16));
    dto.setPublicToggle(true);
    dto.setCompleteToggle(false);
  }

  @Test
  void testValidation() {
    Set<ConstraintViolation<CalendarEventDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isTrue();
  }
  
  @Test
  void testDateOfEventNotNull() {
    dto.setDateOfEvent(null);
    
    Set<ConstraintViolation<CalendarEventDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<CalendarEventDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{date.time.not.null}");
    }
  }
  
  @Test
  void testTitleSizeTooSmall() {
    dto.setTitle("");
    
    Set<ConstraintViolation<CalendarEventDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<CalendarEventDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{title.length}");
    }
  }
  
  @Test
  void testTitleSizeTooLarge() {
    dto.setTitle("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    
    Set<ConstraintViolation<CalendarEventDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<CalendarEventDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{title.length}");
    }
  }
}
