package com.thomaslent.tcltravels.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

  public AdminEmployeeService(EmployeeRepository employeeRepository, PersonRepository personRepository,
      UserRepository userRepository) {
    this.employeeRepository = employeeRepository;
    this.personRepository = personRepository;
    this.userRepository = userRepository;
  }

  public List<AdminEmployeeView> getEmployees() {
    return employeeRepository.findAllByOrderByPersonLastNameAscPersonFirstNameAsc()
        .stream()
        .map(this::toAdminEmployeeView)
        .collect(Collectors.toList());
  }

  public EmployeeEditForm getEmployeeEditForm(Long personId) {
    Person person = personRepository.findById(personId).orElse(null);
    if (person == null) {
      return null;
    }
    Optional<Employee> employeeOpt = employeeRepository.findByPerson(person);
    if (employeeOpt.isEmpty()) {
      return null;
    }
    Employee employee = employeeOpt.get();
    EmployeeEditForm form = new EmployeeEditForm();
    form.setFirstName(person.getFirstName());
    form.setLastName(person.getLastName());
    form.setAddress(person.getAddress());
    form.setCity(person.getCity());
    form.setState(person.getState());
    form.setZipcode(person.getZipCode() != null ? person.getZipCode().toString() : "");
    form.setTelephone(person.getTelephone());
    form.setSsn(employee.getSsn() != null ? employee.getSsn().toString() : "");
    form.setStartDate(employee.getStartDate() != null ? employee.getStartDate().format(DATE_FORMATTER) : "");
    form.setHourlyRate(employee.getHourlyRate() != null ? employee.getHourlyRate().toString() : "");
    form.setIsManager(Boolean.TRUE.equals(employee.getIsManager()));
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
    person.setZipCode(Integer.parseInt(form.getZipcode()));
    person = personRepository.save(person);

    User user = new User();
    user.setUsername(form.getEmail());
    user.setPassword(form.getPassword());
    user.setPerson(person);
    userRepository.save(user);

    Employee employee = new Employee();
    employee.setPerson(person);
    employee.setSsn(Integer.parseInt(form.getSsn()));
    employee.setIsManager(form.getIsManager());
    employee.setStartDate(LocalDate.parse(form.getStartDate(), DATE_FORMATTER));
    employee.setHourlyRate(new BigDecimal(form.getHourlyRate()));
    employeeRepository.save(employee);
  }

  @Transactional
  public boolean updateEmployee(Long personId, EmployeeEditForm form) {
    Person person = personRepository.findById(personId).orElse(null);
    if (person == null) {
      return false;
    }
    Optional<Employee> employeeOpt = employeeRepository.findByPerson(person);
    if (employeeOpt.isEmpty()) {
      return false;
    }
    person.setFirstName(form.getFirstName());
    person.setLastName(form.getLastName());
    person.setAddress(form.getAddress());
    person.setCity(form.getCity());
    person.setState(form.getState().toUpperCase());
    person.setTelephone(form.getTelephone());
    person.setZipCode(Integer.parseInt(form.getZipcode()));
    personRepository.save(person);

    Integer newSsn = Integer.parseInt(form.getSsn());
    LocalDate startDate = LocalDate.parse(form.getStartDate(), DATE_FORMATTER);
    BigDecimal hourlyRate = new BigDecimal(form.getHourlyRate());
    employeeRepository.updateEmployeeByPersonId(personId, newSsn, form.getIsManager(), startDate, hourlyRate);
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
    Person person = employee.getPerson();
    return new AdminEmployeeView(
        person.getId(),
        person.getFirstName(),
        person.getLastName(),
        person.getTelephone(),
        employee.getSsn(),
        employee.getIsManager(),
        employee.getHourlyRate());
  }
}
