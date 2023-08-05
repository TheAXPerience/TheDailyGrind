package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.MessageSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

// @RunWith(SpringRunner.class)
// @SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class TestSearchQueryDtoValidation {
  private SearchQueryDto dto;
  
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
    dto = new SearchQueryDto();
    dto.setQuery("my query");
    dto.setPage(0);
    dto.setSize(10);
  }
  
  @Test
  void testValidation() {
    Set<ConstraintViolation<SearchQueryDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isTrue();
  }
  
  @Test
  void testQueryNotEmpty() {
    dto.setQuery("");
    
    Set<ConstraintViolation<SearchQueryDto>> violations = validator.validate(dto);
    assertThat(violations.isEmpty()).isFalse();
    
    for (ConstraintViolation<SearchQueryDto> v : violations) {
      assertThat(v.getMessage()).isEqualTo("{query.empty}");
    }
  }
}
