package com.thomaslent.tcltravels.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thomaslent.tcltravels.dto.AuthResponse;
import com.thomaslent.tcltravels.dto.LoginRequest;
import com.thomaslent.tcltravels.dto.RefreshTokenRequest;
import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.dto.responses.ErrorResponse;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.security.jwt.JwtTokenProvider;
import com.thomaslent.tcltravels.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final UserService userService;

  public AuthController(AuthenticationManager authenticationManager,
      JwtTokenProvider tokenProvider,
      UserService userService) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getEmail(),
              loginRequest.getPassword()));

      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

      String accessToken = tokenProvider.generateAccessToken(authentication);
      String refreshToken = tokenProvider.generateRefreshToken(authentication);

      AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
          userPrincipal.getUserId(),
          userPrincipal.getPersonId(),
          userPrincipal.getCustomerId(),
          userPrincipal.getFirstName(),
          userPrincipal.getLastName(),
          userPrincipal.getUsername(),
          userPrincipal.getAuthorities().iterator().next().getAuthority(),
          userPrincipal.getRoleLabel());

      AuthResponse response = new AuthResponse(accessToken, refreshToken, userInfo);

      return ResponseEntity.ok(response);
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("Invalid email or password"));
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
    try {
      // Register the new customer
      userService.registerNewCustomer(userDto);

      // Auto-login after registration
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              userDto.getEmail(),
              userDto.getPassword()));

      UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

      String accessToken = tokenProvider.generateAccessToken(authentication);
      String refreshToken = tokenProvider.generateRefreshToken(authentication);

      AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
          userPrincipal.getUserId(),
          userPrincipal.getPersonId(),
          userPrincipal.getCustomerId(),
          userPrincipal.getFirstName(),
          userPrincipal.getLastName(),
          userPrincipal.getUsername(),
          userPrincipal.getAuthorities().iterator().next().getAuthority(),
          userPrincipal.getRoleLabel());

      AuthResponse response = new AuthResponse(accessToken, refreshToken, userInfo);

      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ErrorResponse("Registration failed: " + e.getMessage()));
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest request) {
    try {
      String refreshToken = request.getRefreshToken();

      if (!tokenProvider.validateToken(refreshToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("Invalid or expired refresh token"));
      }

      // Reconstruct user principal from validated token claims
      UserPrincipal userPrincipal = tokenProvider.getUserPrincipalFromToken(refreshToken);

      // Create authentication object for new access token generation
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          userPrincipal,
          null,
          userPrincipal.getAuthorities());

      String newAccessToken = tokenProvider.generateAccessToken(authentication);

      return ResponseEntity.ok(new RefreshResponse(newAccessToken));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ErrorResponse("Failed to refresh token"));
    }
  }

  private static class RefreshResponse {
    private String accessToken;

    public RefreshResponse(String accessToken) {
      this.accessToken = accessToken;
    }

    public String getAccessToken() {
      return accessToken;
    }

    public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
    }
  }
}
