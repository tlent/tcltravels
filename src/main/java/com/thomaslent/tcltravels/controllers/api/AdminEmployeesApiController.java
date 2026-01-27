package com.thomaslent.tcltravels.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thomaslent.tcltravels.dto.AdminEmployeeView;
import com.thomaslent.tcltravels.dto.EmployeeCreateForm;
import com.thomaslent.tcltravels.dto.EmployeeEditForm;
import com.thomaslent.tcltravels.dto.responses.ErrorResponse;
import com.thomaslent.tcltravels.dto.responses.SuccessResponse;
import com.thomaslent.tcltravels.services.AdminEmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/employees")
@PreAuthorize("hasRole('MANAGER')")
@Validated
public class AdminEmployeesApiController {

  private final AdminEmployeeService employeeService;

  public AdminEmployeesApiController(AdminEmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping
  public ResponseEntity<List<AdminEmployeeView>> getEmployees() {
    List<AdminEmployeeView> employees = employeeService.getEmployees();
    return ResponseEntity.ok(employees);
  }

  @GetMapping("/{personId}")
  public ResponseEntity<?> getEmployee(@PathVariable Long personId) {
    EmployeeEditForm employee = employeeService.getEmployeeEditForm(personId);
    if (employee == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(employee);
  }

  @PostMapping
  public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeCreateForm createForm) {
    // Check if username already exists
    if (employeeService.usernameExists(createForm.getEmail())) {
      return ResponseEntity.badRequest()
          .body(new ErrorResponse("An account with this email already exists"));
    }

    try {
      employeeService.createEmployee(createForm);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new SuccessResponse("Employee created successfully"));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(new ErrorResponse("Failed to create employee: " + e.getMessage()));
    }
  }

  @PutMapping("/{personId}")
  public ResponseEntity<?> updateEmployee(
      @PathVariable Long personId,
      @Valid @RequestBody EmployeeEditForm editForm) {

    try {
      boolean success = employeeService.updateEmployee(personId, editForm);
      if (!success) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(new SuccessResponse("Employee updated successfully"));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(new ErrorResponse("Failed to update employee: " + e.getMessage()));
    }
  }

  @DeleteMapping("/{personId}")
  public ResponseEntity<?> deleteEmployee(@PathVariable Long personId) {
    boolean success = employeeService.deleteEmployee(personId);
    if (!success) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(new SuccessResponse("Employee deleted successfully"));
  }
}
