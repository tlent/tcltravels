package com.thomaslent.tcltravels.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.thomaslent.tcltravels.dto.EmployeeCreateForm;
import com.thomaslent.tcltravels.dto.EmployeeEditForm;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.AdminEmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/employees")
@PreAuthorize("hasRole('MANAGER')")
public class AdminEmployeesController {
  private AdminEmployeeService adminEmployeeService;

  public AdminEmployeesController(AdminEmployeeService adminEmployeeService) {
    this.adminEmployeeService = adminEmployeeService;
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("employeeForm", new EmployeeCreateForm());
    return "admin/employees/new";
  }

  @PostMapping
  public String createEmployee(@Valid @ModelAttribute("employeeForm") EmployeeCreateForm form,
      BindingResult bindingResult) {
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
  public String showEditForm(@PathVariable Long personId, Model model) {
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
      BindingResult bindingResult, @AuthenticationPrincipal UserPrincipal principal, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("personId", personId);
      return "admin/employees/edit";
    }
    boolean updated = adminEmployeeService.updateEmployee(personId, form);
    if (!updated) {
      return "redirect:/admin";
    }
    if (personId.equals(principal.getPersonId())) {
      return "redirect:/admin";
    }
    return "redirect:/admin";
  }

  @GetMapping("/{personId}/delete")
  public String showDelete(@PathVariable Long personId, @AuthenticationPrincipal UserPrincipal principal, Model model) {
    if (personId.equals(principal.getPersonId())) {
      return "redirect:/admin";
    }
    model.addAttribute("personId", personId);
    return "admin/employees/delete";
  }

  @PostMapping("/{personId}/delete")
  public String deleteEmployee(@PathVariable Long personId, @AuthenticationPrincipal UserPrincipal principal) {
    if (personId.equals(principal.getPersonId())) {
      return "redirect:/admin";
    }
    adminEmployeeService.deleteEmployee(personId);
    return "redirect:/admin";
  }
}
