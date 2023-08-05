package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;
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
public class TestMonthYearDtoValidation {
  private MonthYearDto dto;
  
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
    dto = new MonthYearDto();
    dto.setMonthYear(YearMonth.of(2023, 8));
    dto.setEventPage(0);
    dto.setEventSize(10);
    dto.setDiaryPage(0);
    dto.setDiarySize(10);
  }
  
  @Test
  void testValidation() {
    Set<ConstraintViolation<MonthYearDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isTrue();
  }
  
  @Test
  void testMonthYearNotNull() {
    dto.setMonthYear(null);
    
    Set<ConstraintViolation<MonthYearDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<MonthYearDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{month.year.not.null}");
    }
  }
}
