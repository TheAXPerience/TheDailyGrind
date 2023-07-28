package com.alvinxu.TheDailyGrind.dto;

import jakarta.validation.constraints.NotEmpty;

public class SearchQueryDto {
  @NotEmpty
  private String query;

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }
  
}
