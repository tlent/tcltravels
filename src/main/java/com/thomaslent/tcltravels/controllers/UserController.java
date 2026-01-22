package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {
  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showRegisterPage(@AuthenticationPrincipal UserPrincipal principal, Model model) {
    if (principal != null) {
      boolean isCustomer = principal.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .anyMatch("ROLE_CUSTOMER"::equals);
      return isCustomer ? "redirect:/" : "redirect:/admin";
    }
    model.addAttribute("registerForm", new UserDto());
    return "register";
  }

  @PostMapping("/register")
  public String register(@Valid @ModelAttribute("registerForm") UserDto registrationDto,
      BindingResult bindingResult) {
    String password = registrationDto.getPassword();
    if (password == null || password.length() < 6) {
      bindingResult.rejectValue("password", "error.registrationDto", "Password must be at least 6 characters");
    }
    if (bindingResult.hasErrors()) {
      return "register";
    }
    userService.registerNewCustomer(registrationDto);
    return "redirect:/login?register=true";
  }

  @GetMapping("/account")
  @PreAuthorize("hasRole('CUSTOMER')")
  public String showAccountPage(@AuthenticationPrincipal UserPrincipal principal, Model model) {
    UserDto userDto = userService.getUserDto(principal.getPersonId());
    model.addAttribute("editUserForm", userDto);
    return "account";
  }

  @PostMapping("/account")
  @PreAuthorize("hasRole('CUSTOMER')")
  public String editUser(@Valid @ModelAttribute("editUserForm") UserDto userDto, BindingResult bindingResult,
      @AuthenticationPrincipal UserPrincipal principal, Model model) {
    if (bindingResult.hasErrors()) {
      return "account";
    }
    boolean updated = userService.editCustomer(principal.getPersonId(), userDto);
    if (!updated) {
      bindingResult.rejectValue("password", "error.userDto", "Wrong Password");
      return "account";
    }
    UserDto updatedUserDto = userService.getUserDto(principal.getPersonId());
    model.addAttribute("editUserForm", updatedUserDto);
    return "account";
  }
}
