package com.thomaslent.tcltravels.controllers.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.thomaslent.tcltravels.dto.AuthResponse;
import com.thomaslent.tcltravels.dto.LoginRequest;
import com.thomaslent.tcltravels.dto.RefreshTokenRequest;
import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.dto.responses.ErrorResponse;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.security.jwt.JwtTokenProvider;
import com.thomaslent.tcltravels.services.UserService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtTokenProvider tokenProvider;

  @Mock
  private UserService userService;

  @InjectMocks
  private AuthController authController;

  @Test
  void login_withValidCredentials_returnsAuthResponse() {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("Password123");

    UserPrincipal userPrincipal = createCustomerUserPrincipal();
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userPrincipal, null, userPrincipal.getAuthorities());

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(tokenProvider.generateAccessToken(authentication)).thenReturn("access-token");
    when(tokenProvider.generateRefreshToken(authentication)).thenReturn("refresh-token");

    ResponseEntity<?> response = authController.login(loginRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    AuthResponse authResponse = (AuthResponse) response.getBody();
    assertThat(authResponse).isNotNull();
    assertThat(authResponse.getAccessToken()).isEqualTo("access-token");
    assertThat(authResponse.getRefreshToken()).isEqualTo("refresh-token");
    assertThat(authResponse.getUser().getEmail()).isEqualTo("test@example.com");
    assertThat(authResponse.getUser().getFirstName()).isEqualTo("John");
    assertThat(authResponse.getUser().getLastName()).isEqualTo("Doe");
    assertThat(authResponse.getUser().getRole()).isEqualTo("ROLE_CUSTOMER");
    assertThat(authResponse.getUser().getRoleLabel()).isEqualTo("Customer");
  }

  @Test
  void login_withInvalidCredentials_returnsUnauthorized() {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("wrong-password");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    ResponseEntity<?> response = authController.login(loginRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    ErrorResponse error = (ErrorResponse) response.getBody();
    assertThat(error).isNotNull();
    assertThat(error.getError()).isEqualTo("Invalid email or password");
  }

  @Test
  void register_withValidData_returnsCreatedAndAuthResponse() {
    UserDto userDto = createValidUserDto();

    UserPrincipal userPrincipal = createCustomerUserPrincipal();
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userPrincipal, null, userPrincipal.getAuthorities());

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(tokenProvider.generateAccessToken(authentication)).thenReturn("access-token");
    when(tokenProvider.generateRefreshToken(authentication)).thenReturn("refresh-token");

    ResponseEntity<?> response = authController.register(userDto);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    AuthResponse authResponse = (AuthResponse) response.getBody();
    assertThat(authResponse).isNotNull();
    assertThat(authResponse.getAccessToken()).isEqualTo("access-token");
    assertThat(authResponse.getRefreshToken()).isEqualTo("refresh-token");
    assertThat(authResponse.getUser().getEmail()).isEqualTo("test@example.com");

    verify(userService).registerNewCustomer(any(UserDto.class));
  }

  @Test
  void register_withDuplicateEmail_returnsBadRequest() {
    UserDto userDto = createValidUserDto();

    doThrow(new RuntimeException("Email already exists"))
        .when(userService).registerNewCustomer(any(UserDto.class));

    ResponseEntity<?> response = authController.register(userDto);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    ErrorResponse error = (ErrorResponse) response.getBody();
    assertThat(error).isNotNull();
    assertThat(error.getError()).contains("Registration failed");
    assertThat(error.getError()).contains("Email already exists");
  }

  @Test
  void refresh_withValidToken_returnsNewAccessToken() {
    RefreshTokenRequest request = new RefreshTokenRequest();
    request.setRefreshToken("valid-refresh-token");

    UserPrincipal userPrincipal = createCustomerUserPrincipal();

    when(tokenProvider.validateToken("valid-refresh-token")).thenReturn(true);
    when(tokenProvider.getUserPrincipalFromToken("valid-refresh-token")).thenReturn(userPrincipal);
    when(tokenProvider.generateAccessToken(any(Authentication.class))).thenReturn("new-access-token");

    ResponseEntity<?> response = authController.refresh(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();

    verify(tokenProvider).validateToken("valid-refresh-token");
    verify(tokenProvider).getUserPrincipalFromToken("valid-refresh-token");
    verify(tokenProvider).generateAccessToken(any(Authentication.class));
  }

  @Test
  void refresh_withInvalidToken_returnsUnauthorized() {
    RefreshTokenRequest request = new RefreshTokenRequest();
    request.setRefreshToken("invalid-token");

    when(tokenProvider.validateToken("invalid-token")).thenReturn(false);

    ResponseEntity<?> response = authController.refresh(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    ErrorResponse error = (ErrorResponse) response.getBody();
    assertThat(error).isNotNull();
    assertThat(error.getError()).isEqualTo("Invalid or expired refresh token");
  }

  @Test
  void refresh_withExpiredToken_returnsUnauthorized() {
    RefreshTokenRequest request = new RefreshTokenRequest();
    request.setRefreshToken("expired-token");

    when(tokenProvider.validateToken("expired-token")).thenReturn(false);

    ResponseEntity<?> response = authController.refresh(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    ErrorResponse error = (ErrorResponse) response.getBody();
    assertThat(error).isNotNull();
    assertThat(error.getError()).isEqualTo("Invalid or expired refresh token");
  }

  @Test
  void refresh_whenExceptionThrown_returnsUnauthorized() {
    RefreshTokenRequest request = new RefreshTokenRequest();
    request.setRefreshToken("valid-refresh-token");

    when(tokenProvider.validateToken(anyString())).thenReturn(true);
    when(tokenProvider.getUserPrincipalFromToken(anyString()))
        .thenThrow(new RuntimeException("Unexpected error"));

    ResponseEntity<?> response = authController.refresh(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    ErrorResponse error = (ErrorResponse) response.getBody();
    assertThat(error).isNotNull();
    assertThat(error.getError()).isEqualTo("Failed to refresh token");
  }

  private UserPrincipal createCustomerUserPrincipal() {
    return new UserPrincipal(
        1L,
        2L,
        3L,
        "John",
        "Doe",
        "test@example.com",
        "hashed-password",
        "Customer",
        Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
    );
  }

  private UserDto createValidUserDto() {
    UserDto userDto = new UserDto();
    userDto.setFirstName("John");
    userDto.setLastName("Doe");
    userDto.setEmail("test@example.com");
    userDto.setPassword("Password123");
    userDto.setAddress("123 Main St");
    userDto.setCity("Boston");
    userDto.setState("MA");
    userDto.setZipcode("02110");
    userDto.setTelephone("1234567890");
    return userDto;
  }
}
