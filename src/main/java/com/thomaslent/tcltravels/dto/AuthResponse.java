package com.thomaslent.tcltravels.dto;

public class AuthResponse {
  private String accessToken;
  private String refreshToken;
  private UserInfo user;

  public AuthResponse(String accessToken, String refreshToken, UserInfo user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.user = user;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public UserInfo getUser() {
    return user;
  }

  public void setUser(UserInfo user) {
    this.user = user;
  }

  public static class UserInfo {
    private Long id;
    private Long personId;
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String roleLabel;

    public UserInfo(Long id, Long personId, Long customerId, String firstName, String lastName,
        String email, String role, String roleLabel) {
      this.id = id;
      this.personId = personId;
      this.customerId = customerId;
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.role = role;
      this.roleLabel = roleLabel;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Long getPersonId() {
      return personId;
    }

    public void setPersonId(Long personId) {
      this.personId = personId;
    }

    public Long getCustomerId() {
      return customerId;
    }

    public void setCustomerId(Long customerId) {
      this.customerId = customerId;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public String getRoleLabel() {
      return roleLabel;
    }

    public void setRoleLabel(String roleLabel) {
      this.roleLabel = roleLabel;
    }
  }
}
