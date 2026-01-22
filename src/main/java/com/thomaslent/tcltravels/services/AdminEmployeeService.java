package com.thomaslent.tcltravels.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thomaslent.tcltravels.dto.AdminEmployeeView;
import com.thomaslent.tcltravels.dto.EmployeeCreateForm;
import com.thomaslent.tcltravels.dto.EmployeeEditForm;
import com.thomaslent.tcltravels.entities.Employee;
import com.thomaslent.tcltravels.entities.Person;
import com.thomaslent.tcltravels.entities.User;
import com.thomaslent.tcltravels.repositories.EmployeeRepository;
import com.thomaslent.tcltravels.repositories.PersonRepository;
import com.thomaslent.tcltravels.repositories.UserRepository;

@Service
public class AdminEmployeeService {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  private EmployeeRepository employeeRepository;
  private PersonRepository personRepository;
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  public AdminEmployeeService(EmployeeRepository employeeRepository, PersonRepository personRepository,
      UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.employeeRepository = employeeRepository;
    this.personRepository = personRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<AdminEmployeeView> getEmployees() {
    return employeeRepository.findAllByOrderByAccountPersonLastNameAscAccountPersonFirstNameAsc()
        .stream()
        .map(this::toAdminEmployeeView)
        .collect(Collectors.toList());
  }

  public EmployeeEditForm getEmployeeEditForm(Long personId) {
    Optional<Employee> employeeOpt = employeeRepository.findByAccountPersonId(personId);
    if (employeeOpt.isEmpty()) {
      return null;
    }
    Employee employee = employeeOpt.get();
    Person person = employee.getAccount().getPerson();
    EmployeeEditForm form = new EmployeeEditForm();
    form.setFirstName(person.getFirstName());
    form.setLastName(person.getLastName());
    form.setAddress(person.getAddress());
    form.setCity(person.getCity());
    form.setState(person.getState());
    form.setZipcode(person.getZipCode() != null ? person.getZipCode() : "");
    form.setTelephone(person.getTelephone());
    form.setSsn(employee.getSsn() != null ? employee.getSsn().toString() : "");
    form.setStartDate(employee.getStartDate() != null ? employee.getStartDate().format(DATE_FORMATTER) : "");
    form.setHourlyRate(employee.getHourlyRate() != null ? employee.getHourlyRate().toString() : "");
    form.setIsManager("MANAGER".equals(employee.getAccount().getRole()));
    return form;
  }

  @Transactional
  public void createEmployee(EmployeeCreateForm form) {
    Person person = new Person();
    person.setFirstName(form.getFirstName());
    person.setLastName(form.getLastName());
    person.setAddress(form.getAddress());
    person.setCity(form.getCity());
    person.setState(form.getState().toUpperCase());
    person.setTelephone(form.getTelephone());
    person.setZipCode(form.getZipcode());
    person = personRepository.save(person);

    User user = new User();
    user.setUsername(form.getEmail());
    user.setPassword(passwordEncoder.encode(form.getPassword()));
    user.setRole(form.getIsManager() ? "MANAGER" : "EMPLOYEE");
    user.setPerson(person);
    userRepository.save(user);

    Employee employee = new Employee();
    employee.setAccount(user);
    employee.setSsn(Integer.parseInt(form.getSsn()));
    employee.setStartDate(LocalDate.parse(form.getStartDate(), DATE_FORMATTER));
    employee.setHourlyRate(new BigDecimal(form.getHourlyRate()));
    employeeRepository.save(employee);
  }

  @Transactional
  public boolean updateEmployee(Long personId, EmployeeEditForm form) {
    Optional<Employee> employeeOpt = employeeRepository.findByAccountPersonId(personId);
    if (employeeOpt.isEmpty()) {
      return false;
    }
    Employee employee = employeeOpt.get();
    Person person = employee.getAccount().getPerson();
    person.setFirstName(form.getFirstName());
    person.setLastName(form.getLastName());
    person.setAddress(form.getAddress());
    person.setCity(form.getCity());
    person.setState(form.getState().toUpperCase());
    person.setTelephone(form.getTelephone());
    person.setZipCode(form.getZipcode());
    personRepository.save(person);

    employee.setSsn(Integer.parseInt(form.getSsn()));
    employee.setStartDate(LocalDate.parse(form.getStartDate(), DATE_FORMATTER));
    employee.setHourlyRate(new BigDecimal(form.getHourlyRate()));
    employeeRepository.save(employee);

    User user = employee.getAccount();
    user.setRole(form.getIsManager() ? "MANAGER" : "EMPLOYEE");
    userRepository.save(user);
    return true;
  }

  @Transactional
  public boolean deleteEmployee(Long personId) {
    Person person = personRepository.findById(personId).orElse(null);
    if (person == null) {
      return false;
    }
    personRepository.delete(person);
    return true;
  }

  public boolean usernameExists(String username) {
    return userRepository.existsByUsername(username);
  }

  private AdminEmployeeView toAdminEmployeeView(Employee employee) {
    Person person = employee.getAccount().getPerson();
    return new AdminEmployeeView(
        person.getId(),
        person.getFirstName(),
        person.getLastName(),
        person.getTelephone(),
        employee.getSsn(),
        "MANAGER".equals(employee.getAccount().getRole()),
        employee.getHourlyRate());
  }
}
