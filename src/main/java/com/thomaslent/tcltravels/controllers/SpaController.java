package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to forward all frontend routes to index.html for React client-side routing.
 * This enables the React app to handle its own routing without 404 errors.
 */
@Controller
public class SpaController {

  @GetMapping(value = {
      "/",
      "/login",
      "/register",
      "/account",
      "/flights",
      "/flights/**",
      "/reservations",
      "/reservations/**",
      "/admin",
      "/admin/**"
  })
  public String forward() {
    return "forward:/index.html";
  }
}
