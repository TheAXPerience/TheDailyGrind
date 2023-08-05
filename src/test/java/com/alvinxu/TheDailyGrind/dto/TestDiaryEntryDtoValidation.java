package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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
public class TestDiaryEntryDtoValidation {
  private DiaryEntryDto dto;
  
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
    dto = new DiaryEntryDto();
    dto.setTitle("my title");
    dto.setEntry("my entry");
    dto.setDate_of_entry(LocalDate.of(2023, 4, 20));
  }

  @Test
  void testValidation() {
    Set<ConstraintViolation<DiaryEntryDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isTrue();
  }
  
  @Test
  void testTitleSizeTooSmall() {
    dto.setTitle("");
    
    Set<ConstraintViolation<DiaryEntryDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<DiaryEntryDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{title.length}");
    }
  }
  
  @Test
  void testTitleSizeTooLarge() {
    dto.setTitle("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
    
    Set<ConstraintViolation<DiaryEntryDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<DiaryEntryDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{title.length}");
    }
  }
  
  @Test
  void testDateOfEntryNotNull() {
    dto.setDate_of_entry(null);
    
    Set<ConstraintViolation<DiaryEntryDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<DiaryEntryDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{date.not.null}");
    }
  }
  
  @Test
  void testEntryNotBlank() {
    dto.setEntry("");
    
    Set<ConstraintViolation<DiaryEntryDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<DiaryEntryDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{entry.empty}");
    }
  }
}
