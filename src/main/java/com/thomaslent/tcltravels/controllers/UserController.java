package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showRegisterPage(HttpSession session, Model model) {
    if (session.getAttribute("p_id") != null) {
      return "redirect:/";
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
  public String showAccountPage(HttpSession session, Model model) {
    Long id = (Long) session.getAttribute("p_id");
    if (id == null) {
      return "redirect:/login";
    }
    if (!session.getAttribute("role").equals("Customer")) {
      return "redirect:/";
    }
    UserDto userDto = userService.getUserDto(id);
    model.addAttribute("editUserForm", userDto);
    return "account";
  }

  @PostMapping("/account")
  public String editUser(@Valid @ModelAttribute("editUserForm") UserDto userDto, BindingResult bindingResult,
      HttpSession session, Model model) {
    Long id = (Long) session.getAttribute("p_id");
    if (id == null) {
      return "redirect:/login";
    }
    if (!session.getAttribute("role").equals("Customer")) {
      return "redirect:/";
    }
    if (bindingResult.hasErrors()) {
      return "account";
    }
    boolean updated = userService.editCustomer(id, userDto);
    if (!updated) {
      bindingResult.rejectValue("password", "error.userDto", "Wrong Password");
      return "account";
    }
    UserDto updatedUserDto = userService.getUserDto(id);
    session.setAttribute("p_firstName", updatedUserDto.getFirstName());
    session.setAttribute("p_lastName", updatedUserDto.getLastName());
    model.addAttribute("editUserForm", updatedUserDto);
    return "account";
  }
}