package com.thomaslent.tcltravels.exceptions;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceType, Long id) {
    super(String.format("%s not found with id: %d", resourceType, id));
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
