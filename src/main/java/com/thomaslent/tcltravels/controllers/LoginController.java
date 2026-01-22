package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.thomaslent.tcltravels.security.UserPrincipal;

@Controller
public class LoginController {
  @GetMapping("/login")
  public String showLoginPage(@AuthenticationPrincipal UserPrincipal principal) {
    if (principal != null) {
      boolean isCustomer = principal.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .anyMatch("ROLE_CUSTOMER"::equals);
      return isCustomer ? "redirect:/" : "redirect:/admin";
    }
    return "login";
  }
}
