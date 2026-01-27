package com.thomaslent.tcltravels.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.dto.responses.ErrorResponse;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@PreAuthorize("hasRole('CUSTOMER')")
public class UserApiController {

  private final UserService userService;

  public UserApiController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    UserDto userDto = userService.getUserDto(userPrincipal.getPersonId());
    return ResponseEntity.ok(userDto);
  }

  @PutMapping("/me")
  public ResponseEntity<?> updateCurrentUser(
      @Valid @RequestBody UserDto userDto,
      Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    boolean success = userService.editCustomer(userPrincipal.getPersonId(), userDto);

    if (!success) {
      return ResponseEntity.badRequest()
          .body(new ErrorResponse("Invalid password or update failed"));
    }

    UserDto updatedUser = userService.getUserDto(userPrincipal.getPersonId());
    return ResponseEntity.ok(updatedUser);
  }
}
