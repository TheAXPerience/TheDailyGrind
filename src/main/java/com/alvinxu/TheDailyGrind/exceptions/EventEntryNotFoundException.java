package com.alvinxu.TheDailyGrind.exceptions;

public class EventEntryNotFoundException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -3126142436299565149L;

  public EventEntryNotFoundException(String message) {
    super(message);
  }
}
