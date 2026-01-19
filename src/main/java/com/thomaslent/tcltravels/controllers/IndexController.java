package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class IndexController {
  @GetMapping("/")
  public String index(HttpSession session) {
    if (session.getAttribute("p_id") == null) {
      return "redirect:/login";
    }
    String role = (String) session.getAttribute("role");
    if ("Employee".equals(role) || "Admin".equals(role)) {
      return "redirect:/admin/";
    }
    return "index";
  }

  @GetMapping("/help")
  public String help(HttpSession session) {
    if (session.getAttribute("p_id") == null) {
      return "redirect:/login";
    }
    return "help";
  }
}
