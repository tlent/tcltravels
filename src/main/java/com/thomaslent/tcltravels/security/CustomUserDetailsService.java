package com.thomaslent.tcltravels.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.User;
import com.thomaslent.tcltravels.repositories.CustomerRepository;
import com.thomaslent.tcltravels.repositories.EmployeeRepository;
import com.thomaslent.tcltravels.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final CustomerRepository customerRepository;
  private final EmployeeRepository employeeRepository;

  public CustomUserDetailsService(UserRepository userRepository,
      CustomerRepository customerRepository,
      EmployeeRepository employeeRepository) {
    this.userRepository = userRepository;
    this.customerRepository = customerRepository;
    this.employeeRepository = employeeRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    List<GrantedAuthority> authorities = new ArrayList<>();
    String role = user.getRole();
    String roleLabel;
    Long customerId = null;

    if ("CUSTOMER".equals(role)) {
      Customer customer = customerRepository.findByAccountId(user.getId())
          .orElseThrow(() -> new UsernameNotFoundException("Customer record missing"));
      customerId = customer.getId();
      authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
      roleLabel = "Customer";
    } else if ("MANAGER".equals(role)) {
      employeeRepository.findByAccountId(user.getId())
          .orElseThrow(() -> new UsernameNotFoundException("Employee record missing"));
      authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
      roleLabel = "Manager";
    } else {
      employeeRepository.findByAccountId(user.getId())
          .orElseThrow(() -> new UsernameNotFoundException("Employee record missing"));
      authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
      roleLabel = "Employee";
    }

    return new UserPrincipal(user.getId(), user.getPerson().getId(), customerId,
        user.getPerson().getFirstName(), user.getPerson().getLastName(),
        user.getUsername(), user.getPassword(), roleLabel, authorities);
  }
}
