package com.alvinxu.TheDailyGrind.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -2530701291665324382L;

  public UsernameAlreadyExistsException(String message) {
    super(message);
  }

}
