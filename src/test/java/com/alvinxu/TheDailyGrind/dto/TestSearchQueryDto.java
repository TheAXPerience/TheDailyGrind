package com.alvinxu.TheDailyGrind.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TestSearchQueryDto {
  @Test
  void testQuery() {
    SearchQueryDto dto = new SearchQueryDto();
    String query = "query my love";
    
    dto.setQuery(query);
    assertThat(dto.getQuery()).isEqualTo(query);
  }
  
  @Test
  void testPage() {
    SearchQueryDto dto = new SearchQueryDto();
    
    dto.setPage(56);
    assertThat(dto.getPage()).isEqualTo(56);
  }
  
  @Test
  void testSize() {
    SearchQueryDto dto = new SearchQueryDto();
    
    dto.setSize(50);
    assertThat(dto.getSize()).isEqualTo(50);
  }
}
