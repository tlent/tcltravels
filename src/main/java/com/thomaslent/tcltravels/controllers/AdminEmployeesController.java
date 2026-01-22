package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thomaslent.tcltravels.dto.EmployeeCreateForm;
import com.thomaslent.tcltravels.dto.EmployeeEditForm;
import com.thomaslent.tcltravels.services.AdminEmployeeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/employees")
public class AdminEmployeesController {
  private AdminEmployeeService adminEmployeeService;

  public AdminEmployeesController(AdminEmployeeService adminEmployeeService) {
    this.adminEmployeeService = adminEmployeeService;
  }

  @GetMapping("/new")
  public String showCreateForm(HttpSession session, Model model) {
    if (!isManager(session)) {
      return redirectForRole(session);
    }
    model.addAttribute("employeeForm", new EmployeeCreateForm());
    return "admin/employees/new";
  }

  @PostMapping
  public String createEmployee(@Valid @ModelAttribute("employeeForm") EmployeeCreateForm form,
      BindingResult bindingResult, HttpSession session) {
    if (!isManager(session)) {
      return redirectForRole(session);
    }
    if (adminEmployeeService.usernameExists(form.getEmail())) {
      bindingResult.rejectValue("email", "error.employeeForm", "Email already exists");
    }
    if (bindingResult.hasErrors()) {
      return "admin/employees/new";
    }
    adminEmployeeService.createEmployee(form);
    return "redirect:/admin";
  }

  @GetMapping("/{personId}/edit")
  public String showEditForm(@PathVariable Long personId, HttpSession session, Model model) {
    if (!isManager(session)) {
      return redirectForRole(session);
    }
    EmployeeEditForm form = adminEmployeeService.getEmployeeEditForm(personId);
    if (form == null) {
      return "redirect:/admin";
    }
    model.addAttribute("employeeForm", form);
    model.addAttribute("personId", personId);
    return "admin/employees/edit";
  }

  @PostMapping("/{personId}")
  public String updateEmployee(@PathVariable Long personId,
      @Valid @ModelAttribute("employeeForm") EmployeeEditForm form,
      BindingResult bindingResult, HttpSession session, Model model) {
    if (!isManager(session)) {
      return redirectForRole(session);
    }
    if (bindingResult.hasErrors()) {
      model.addAttribute("personId", personId);
      return "admin/employees/edit";
    }
    boolean updated = adminEmployeeService.updateEmployee(personId, form);
    if (!updated) {
      return "redirect:/admin";
    }
    if (personId.equals(session.getAttribute("p_id"))) {
      session.setAttribute("p_firstName", form.getFirstName());
      session.setAttribute("p_lastName", form.getLastName());
    }
    return "redirect:/admin";
  }

  @GetMapping("/{personId}/delete")
  public String showDelete(@PathVariable Long personId, HttpSession session, Model model) {
    if (!isManager(session)) {
      return redirectForRole(session);
    }
    if (personId.equals(session.getAttribute("p_id"))) {
      return "redirect:/admin";
    }
    model.addAttribute("personId", personId);
    return "admin/employees/delete";
  }

  @PostMapping("/{personId}/delete")
  public String deleteEmployee(@PathVariable Long personId, HttpSession session) {
    if (!isManager(session)) {
      return redirectForRole(session);
    }
    if (personId.equals(session.getAttribute("p_id"))) {
      return "redirect:/admin";
    }
    adminEmployeeService.deleteEmployee(personId);
    return "redirect:/admin";
  }

  private boolean isManager(HttpSession session) {
    return session.getAttribute("p_id") != null && "Manager".equals(session.getAttribute("role"));
  }

  private String redirectForRole(HttpSession session) {
    if (session.getAttribute("p_id") == null) {
      return "redirect:/login";
    }
    return "redirect:/";
  }
}
