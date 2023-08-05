package com.alvinxu.TheDailyGrind.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSearchQueryDtoValidation {
  private SearchQueryDto dto;
  
  @BeforeEach
  void setup() {
    dto = new SearchQueryDto();
    dto.setQuery("my query");
    dto.setPage(0);
    dto.setSize(10);
  }
  
  @Test
  void testValidation() {
    
  }
  
  @Test
  void testQueryNotEmpty() {
    dto.setQuery("");
  }
}
