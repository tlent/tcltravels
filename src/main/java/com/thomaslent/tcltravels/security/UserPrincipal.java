package com.thomaslent.tcltravels.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
  private final Long userId;
  private final Long personId;
  private final Long customerId;
  private final String firstName;
  private final String lastName;
  private final String username;
  private final String password;
  private final String roleLabel;
  private final Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(Long userId, Long personId, Long customerId, String firstName, String lastName,
      String username, String password, String roleLabel,
      Collection<? extends GrantedAuthority> authorities) {
    this.userId = userId;
    this.personId = personId;
    this.customerId = customerId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.roleLabel = roleLabel;
    this.authorities = authorities;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getPersonId() {
    return personId;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  public String getRoleLabel() {
    return roleLabel;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
