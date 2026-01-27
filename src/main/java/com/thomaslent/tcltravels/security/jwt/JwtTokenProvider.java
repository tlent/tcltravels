package com.thomaslent.tcltravels.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.thomaslent.tcltravels.security.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
  private static final int MINIMUM_SECRET_LENGTH = 32;

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.access-token-expiration-ms}")
  private long accessTokenExpirationMs;

  @Value("${jwt.refresh-token-expiration-ms}")
  private long refreshTokenExpirationMs;

  @PostConstruct
  public void validateConfiguration() {
    if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
      throw new IllegalStateException(
          "JWT_SECRET environment variable must be set. " +
          "Please configure a secure random string of at least " + MINIMUM_SECRET_LENGTH + " characters."
      );
    }

    if (jwtSecret.length() < MINIMUM_SECRET_LENGTH) {
      throw new IllegalStateException(
          "JWT_SECRET is too short (current: " + jwtSecret.length() + " characters). " +
          "Minimum required: " + MINIMUM_SECRET_LENGTH + " characters for security."
      );
    }

    logger.info("JWT configuration validated successfully (secret length: {} characters)", jwtSecret.length());
  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }

  public String generateAccessToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    return generateToken(userPrincipal, accessTokenExpirationMs);
  }

  public String generateRefreshToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    return generateToken(userPrincipal, refreshTokenExpirationMs);
  }

  private String generateToken(UserPrincipal userPrincipal, long expirationMs) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .subject(userPrincipal.getUsername())
        .claim("userId", userPrincipal.getUserId())
        .claim("personId", userPrincipal.getPersonId())
        .claim("customerId", userPrincipal.getCustomerId())
        .claim("firstName", userPrincipal.getFirstName())
        .claim("lastName", userPrincipal.getLastName())
        .claim("role", userPrincipal.getAuthorities().iterator().next().getAuthority())
        .claim("roleLabel", userPrincipal.getRoleLabel())
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.getSubject();
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("userId", Long.class);
  }

  public Long getPersonIdFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("personId", Long.class);
  }

  public Long getCustomerIdFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    // customerId can be null for employees/managers
    Integer customerId = claims.get("customerId", Integer.class);
    return customerId != null ? customerId.longValue() : null;
  }

  public String getRoleFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("role", String.class);
  }

  public String getRoleLabelFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("roleLabel", String.class);
  }

  public String getFirstNameFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("firstName", String.class);
  }

  public String getLastNameFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.get("lastName", String.class);
  }

  /**
   * Reconstructs a UserPrincipal from a validated JWT token.
   * This should only be called after validateToken() returns true.
   *
   * @param token the validated JWT token
   * @return UserPrincipal containing user details from token claims
   */
  public UserPrincipal getUserPrincipalFromToken(String token) {
    Long userId = getUserIdFromToken(token);
    Long personId = getPersonIdFromToken(token);
    Long customerId = getCustomerIdFromToken(token);
    String firstName = getFirstNameFromToken(token);
    String lastName = getLastNameFromToken(token);
    String username = getUsernameFromToken(token);
    String role = getRoleFromToken(token);
    String roleLabel = getRoleLabelFromToken(token);

    return new UserPrincipal(
        userId,
        personId,
        customerId,
        firstName,
        lastName,
        username,
        null, // password not needed for token refresh
        roleLabel,
        java.util.Collections.singleton(
            new org.springframework.security.core.authority.SimpleGrantedAuthority(role)
        )
    );
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (SignatureException ex) {
      // Invalid JWT signature
    } catch (MalformedJwtException ex) {
      // Invalid JWT token
    } catch (ExpiredJwtException ex) {
      // Expired JWT token
    } catch (UnsupportedJwtException ex) {
      // Unsupported JWT token
    } catch (IllegalArgumentException ex) {
      // JWT claims string is empty
    }
    return false;
  }
}
