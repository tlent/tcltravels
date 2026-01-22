package com.thomaslent.tcltravels.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Employee;
import com.thomaslent.tcltravels.entities.Person;
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

    Person person = user.getPerson();
    Optional<Customer> customerOpt = customerRepository.findByPerson(person);

    List<GrantedAuthority> authorities = new ArrayList<>();
    String roleLabel;
    Long accountNumber = null;

    if (customerOpt.isPresent()) {
      Customer customer = customerOpt.get();
      roleLabel = "Customer";
      accountNumber = customer.getAccountNumber();
      authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    } else {
      Employee employee = employeeRepository.findByPerson(person)
          .orElseThrow(() -> new UsernameNotFoundException("Employee record missing"));
      if (Boolean.TRUE.equals(employee.getIsManager())) {
        roleLabel = "Manager";
        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
      } else {
        roleLabel = "Employee";
        authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
      }
    }

    return new UserPrincipal(person.getId(), accountNumber, person.getFirstName(), person.getLastName(),
        user.getUsername(), user.getPassword(), roleLabel, authorities);
  }
}
