package com.thomaslent.tcltravels.dto.responses;

public class BidResponse {
  private boolean accepted;
  private boolean rejected;
  private String message;

  public BidResponse(boolean accepted, boolean rejected, String message) {
    this.accepted = accepted;
    this.rejected = rejected;
    this.message = message;
  }

  public boolean isAccepted() {
    return accepted;
  }

  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }

  public boolean isRejected() {
    return rejected;
  }

  public void setRejected(boolean rejected) {
    this.rejected = rejected;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
