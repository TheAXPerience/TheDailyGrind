package com.alvinxu.TheDailyGrind.dto;

import jakarta.validation.constraints.NotEmpty;

public class SearchQueryDto {
  @NotEmpty(message="{query.empty}")
  private String query;
  private int page = 0;
  private int size = 10;

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }
  
}
