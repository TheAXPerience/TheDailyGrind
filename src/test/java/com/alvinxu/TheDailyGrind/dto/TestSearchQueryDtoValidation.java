package com.alvinxu.TheDailyGrind.dto;

import org.junit.jupiter.api.BeforeEach;

public class TestSearchQueryDtoValidation {
  private SearchQueryDto dto;
  
  @BeforeEach
  void setup() {
    dto = new SearchQueryDto();
    dto.setQuery("my query");
    dto.setPage(0);
    dto.setSize(10);
  }
  
  void testValidation() {
    
  }
  
  void testQueryNotEmpty() {
    dto.setQuery("");
  }
}
