package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thomaslent.tcltravels.dto.RegistrationDto;
import com.thomaslent.tcltravels.services.RegistrationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {
  private RegistrationService registrationService;

  private RegisterController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @GetMapping
  public String showRegisterPage(HttpSession session, Model model) {
    if (session.getAttribute("p_id") != null) {
      return "redirect:/";
    }
    model.addAttribute("registerForm", new RegistrationDto());
    return "register";
  }

  @PostMapping
  public String register(@Valid @ModelAttribute("registerForm") RegistrationDto registrationDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "register";
    }
    registrationService.registerNewCustomer(registrationDto);
    return "redirect:/login?register=true";
  }

}