package com.thomaslent.tcltravels.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import com.thomaslent.tcltravels.security.UserPrincipal;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtTokenProviderTest {

  private JwtTokenProvider tokenProvider;
  private static final String TEST_SECRET = "test-secret-key-with-at-least-32-characters-for-hmac-sha";

  @BeforeEach
  void setUp() {
    tokenProvider = new JwtTokenProvider();
    ReflectionTestUtils.setField(tokenProvider, "jwtSecret", TEST_SECRET);
    ReflectionTestUtils.setField(tokenProvider, "accessTokenExpirationMs", 900000L); // 15 minutes
    ReflectionTestUtils.setField(tokenProvider, "refreshTokenExpirationMs", 604800000L); // 7 days
    tokenProvider.validateConfiguration();
  }

  @Test
  void validateConfiguration_throwsExceptionWhenSecretIsNull() {
    JwtTokenProvider provider = new JwtTokenProvider();
    ReflectionTestUtils.setField(provider, "jwtSecret", null);
    ReflectionTestUtils.setField(provider, "accessTokenExpirationMs", 900000L);
    ReflectionTestUtils.setField(provider, "refreshTokenExpirationMs", 604800000L);

    assertThatThrownBy(() -> provider.validateConfiguration())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("JWT_SECRET environment variable must be set");
  }

  @Test
  void validateConfiguration_throwsExceptionWhenSecretIsEmpty() {
    JwtTokenProvider provider = new JwtTokenProvider();
    ReflectionTestUtils.setField(provider, "jwtSecret", "   ");
    ReflectionTestUtils.setField(provider, "accessTokenExpirationMs", 900000L);
    ReflectionTestUtils.setField(provider, "refreshTokenExpirationMs", 604800000L);

    assertThatThrownBy(() -> provider.validateConfiguration())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("JWT_SECRET environment variable must be set");
  }

  @Test
  void validateConfiguration_throwsExceptionWhenSecretIsTooShort() {
    JwtTokenProvider provider = new JwtTokenProvider();
    ReflectionTestUtils.setField(provider, "jwtSecret", "short");
    ReflectionTestUtils.setField(provider, "accessTokenExpirationMs", 900000L);
    ReflectionTestUtils.setField(provider, "refreshTokenExpirationMs", 604800000L);

    assertThatThrownBy(() -> provider.validateConfiguration())
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("JWT_SECRET is too short")
        .hasMessageContaining("Minimum required: 32 characters");
  }

  @Test
  void validateConfiguration_succeedsWithValidSecret() {
    JwtTokenProvider provider = new JwtTokenProvider();
    ReflectionTestUtils.setField(provider, "jwtSecret", TEST_SECRET);
    ReflectionTestUtils.setField(provider, "accessTokenExpirationMs", 900000L);
    ReflectionTestUtils.setField(provider, "refreshTokenExpirationMs", 604800000L);

    // Should not throw
    provider.validateConfiguration();
  }

  @Test
  void generateAccessToken_createsValidToken() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    assertThat(token).isNotEmpty();
    assertThat(tokenProvider.validateToken(token)).isTrue();
  }

  @Test
  void generateRefreshToken_createsValidToken() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateRefreshToken(auth);

    assertThat(token).isNotEmpty();
    assertThat(tokenProvider.validateToken(token)).isTrue();
  }

  @Test
  void validateToken_returnsFalseForInvalidToken() {
    assertThat(tokenProvider.validateToken("invalid.token.here")).isFalse();
  }

  @Test
  void validateToken_returnsFalseForTokenWithWrongSignature() {
    // Create token with different secret
    String wrongSecret = "different-secret-key-with-at-least-32-characters-hmac";
    String token = Jwts.builder()
        .subject("test@example.com")
        .signWith(Keys.hmacShaKeyFor(wrongSecret.getBytes()))
        .compact();

    assertThat(tokenProvider.validateToken(token)).isFalse();
  }

  @Test
  void validateToken_returnsFalseForMalformedToken() {
    assertThat(tokenProvider.validateToken("not.a.jwt")).isFalse();
  }

  @Test
  void getUsernameFromToken_extractsCorrectUsername() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    String username = tokenProvider.getUsernameFromToken(token);

    assertThat(username).isEqualTo("test@example.com");
  }

  @Test
  void getUserIdFromToken_extractsCorrectUserId() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    Long userId = tokenProvider.getUserIdFromToken(token);

    assertThat(userId).isEqualTo(100L);
  }

  @Test
  void getPersonIdFromToken_extractsCorrectPersonId() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    Long personId = tokenProvider.getPersonIdFromToken(token);

    assertThat(personId).isEqualTo(200L);
  }

  @Test
  void getCustomerIdFromToken_extractsCorrectCustomerId() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    Long customerId = tokenProvider.getCustomerIdFromToken(token);

    assertThat(customerId).isEqualTo(300L);
  }

  @Test
  void getCustomerIdFromToken_returnsNullForEmployee() {
    Authentication auth = createMockEmployeeAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    Long customerId = tokenProvider.getCustomerIdFromToken(token);

    assertThat(customerId).isNull();
  }

  @Test
  void getRoleFromToken_extractsCorrectRole() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    String role = tokenProvider.getRoleFromToken(token);

    assertThat(role).isEqualTo("ROLE_CUSTOMER");
  }

  @Test
  void getRoleLabelFromToken_extractsCorrectRoleLabel() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    String roleLabel = tokenProvider.getRoleLabelFromToken(token);

    assertThat(roleLabel).isEqualTo("Customer");
  }

  @Test
  void getFirstNameFromToken_extractsCorrectFirstName() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    String firstName = tokenProvider.getFirstNameFromToken(token);

    assertThat(firstName).isEqualTo("John");
  }

  @Test
  void getLastNameFromToken_extractsCorrectLastName() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    String lastName = tokenProvider.getLastNameFromToken(token);

    assertThat(lastName).isEqualTo("Doe");
  }

  @Test
  void getUserPrincipalFromToken_reconstructsUserPrincipalCorrectly() {
    Authentication auth = createMockAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    UserPrincipal userPrincipal = tokenProvider.getUserPrincipalFromToken(token);

    assertThat(userPrincipal.getUserId()).isEqualTo(100L);
    assertThat(userPrincipal.getPersonId()).isEqualTo(200L);
    assertThat(userPrincipal.getCustomerId()).isEqualTo(300L);
    assertThat(userPrincipal.getFirstName()).isEqualTo("John");
    assertThat(userPrincipal.getLastName()).isEqualTo("Doe");
    assertThat(userPrincipal.getUsername()).isEqualTo("test@example.com");
    assertThat(userPrincipal.getRoleLabel()).isEqualTo("Customer");
    assertThat(userPrincipal.getPassword()).isNull();
    assertThat(userPrincipal.getAuthorities()).hasSize(1);
    assertThat(userPrincipal.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_CUSTOMER");
  }

  @Test
  void getUserPrincipalFromToken_handlesEmployeeWithNullCustomerId() {
    Authentication auth = createMockEmployeeAuthentication();
    String token = tokenProvider.generateAccessToken(auth);

    UserPrincipal userPrincipal = tokenProvider.getUserPrincipalFromToken(token);

    assertThat(userPrincipal.getCustomerId()).isNull();
    assertThat(userPrincipal.getRoleLabel()).isEqualTo("Employee");
    assertThat(userPrincipal.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_EMPLOYEE");
  }

  @Test
  void tokenRoundTrip_preservesAllUserInformation() {
    // Create customer authentication
    Authentication auth = createMockAuthentication();

    // Generate access token
    String accessToken = tokenProvider.generateAccessToken(auth);
    assertThat(tokenProvider.validateToken(accessToken)).isTrue();

    // Generate refresh token
    String refreshToken = tokenProvider.generateRefreshToken(auth);
    assertThat(tokenProvider.validateToken(refreshToken)).isTrue();

    // Reconstruct user from access token
    UserPrincipal reconstructedFromAccess = tokenProvider.getUserPrincipalFromToken(accessToken);
    assertThat(reconstructedFromAccess.getUsername()).isEqualTo("test@example.com");
    assertThat(reconstructedFromAccess.getUserId()).isEqualTo(100L);

    // Reconstruct user from refresh token
    UserPrincipal reconstructedFromRefresh = tokenProvider.getUserPrincipalFromToken(refreshToken);
    assertThat(reconstructedFromRefresh.getUsername()).isEqualTo("test@example.com");
    assertThat(reconstructedFromRefresh.getUserId()).isEqualTo(100L);
  }

  private Authentication createMockAuthentication() {
    UserPrincipal userPrincipal = new UserPrincipal(
        100L,
        200L,
        300L,
        "John",
        "Doe",
        "test@example.com",
        "hashed-password",
        "Customer",
        Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
    );

    Authentication auth = mock(Authentication.class);
    when(auth.getPrincipal()).thenReturn(userPrincipal);
    return auth;
  }

  private Authentication createMockEmployeeAuthentication() {
    UserPrincipal userPrincipal = new UserPrincipal(
        101L,
        201L,
        null, // Employee has no customerId
        "Jane",
        "Smith",
        "employee@example.com",
        "hashed-password",
        "Employee",
        Collections.singleton(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
    );

    Authentication auth = mock(Authentication.class);
    when(auth.getPrincipal()).thenReturn(userPrincipal);
    return auth;
  }
}
