package com.thomaslent.tcltravels.controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Employee;
import com.thomaslent.tcltravels.entities.Person;
import com.thomaslent.tcltravels.entities.User;
import com.thomaslent.tcltravels.repositories.CustomerRepository;
import com.thomaslent.tcltravels.repositories.EmployeeRepository;
import com.thomaslent.tcltravels.repositories.UserRepository;

@Controller
@RequestMapping("/login")
public class LoginController {
  private UserRepository userRepository;
  private CustomerRepository customerRepository;
  private EmployeeRepository employeeRepository;

  private LoginController(UserRepository userRepository, CustomerRepository customerRepository,
      EmployeeRepository employeeRepository) {
    this.userRepository = userRepository;
    this.customerRepository = customerRepository;
    this.employeeRepository = employeeRepository;
  }

  @GetMapping
  public String showLoginPage(HttpSession session) {
    if (session.getAttribute("p_id") != null) {
      return "redirect:/";
    }
    return "login";
  }

  @PostMapping
  public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
    if (username.trim().isEmpty() || password.trim().isEmpty()) {
      return "redirect:/login?authValid=0";
    }

    Optional<User> userOpt = userRepository.findByUsername(username);

    if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
      User user = userOpt.get();
      Person person = user.getPerson();
      session.setMaxInactiveInterval(36000);
      session.setAttribute("p_id", person.getId());
      session.setAttribute("p_firstName", person.getFirstName());
      session.setAttribute("p_lastName", person.getLastName());
      session.setAttribute("username", user.getUsername());
      Optional<Customer> customerOpt = customerRepository.findByPerson(person);
      if (customerOpt.isPresent()) {
        session.setAttribute("role", "Customer");
        session.setAttribute("c_accountNumber", customerOpt.get().getAccountNumber());
      } else {
        Optional<Employee> employeeOpt = employeeRepository.findByPerson(person);
        Employee employee = employeeOpt.get();
        if (employee.getIsManager()) {
          session.setAttribute("role", "Manager");
        } else {
          session.setAttribute("role", "Employee");
        }
      }
      return "redirect:/";
    } else {
      return "redirect:/login?authValid=-1";
    }
  }

}