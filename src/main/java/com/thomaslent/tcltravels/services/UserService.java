package com.thomaslent.tcltravels.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Person;
import com.thomaslent.tcltravels.entities.User;
import com.thomaslent.tcltravels.exceptions.ResourceNotFoundException;
import com.thomaslent.tcltravels.repositories.CustomerRepository;
import com.thomaslent.tcltravels.repositories.PersonRepository;
import com.thomaslent.tcltravels.repositories.UserRepository;

@Service
public class UserService {

  private PersonRepository personRepository;
  private UserRepository userRepository;
  private CustomerRepository customerRepository;
  private PasswordEncoder passwordEncoder;

  public UserService(PersonRepository personRepository, UserRepository userRepository,
      CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
    this.personRepository = personRepository;
    this.userRepository = userRepository;
    this.customerRepository = customerRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDto getUserDto(Long id) {
    Person person = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    User user = userRepository.findByPerson(person)
        .orElseThrow(() -> new ResourceNotFoundException("User not found for person id: " + id));

    UserDto userDto = new UserDto();
    userDto.setFirstName(person.getFirstName());
    userDto.setLastName(person.getLastName());
    userDto.setAddress(person.getAddress());
    userDto.setCity(person.getCity());
    userDto.setState(person.getState());
    userDto.setZipcode(person.getZipCode());
    userDto.setTelephone(person.getTelephone());
    userDto.setEmail(user.getUsername());

    return userDto;
  }

  @Transactional
  public void registerNewCustomer(UserDto dto) {
    Person person = new Person();
    person.setFirstName(dto.getFirstName());
    person.setLastName(dto.getLastName());
    person.setAddress(dto.getAddress());
    person.setCity(dto.getCity());
    person.setState(dto.getState().toUpperCase());
    person.setTelephone(dto.getTelephone());
    person.setZipCode(dto.getZipcode());
    person = personRepository.save(person);

    User user = new User();
    user.setUsername(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole("CUSTOMER");
    user.setPerson(person);
    userRepository.save(user);

    Customer customer = new Customer();
    customer.setAccount(user);
    customerRepository.save(customer);
  }

  @Transactional
  public boolean editCustomer(Long id, UserDto dto) {
    Person person = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Person", id));
    User user = userRepository.findByPerson(person)
        .orElseThrow(() -> new ResourceNotFoundException("User not found for person id: " + id));
    String password = dto.getPassword();
    if (password == null || !passwordEncoder.matches(password, user.getPassword())) {
      return false;
    }
    person.setFirstName(dto.getFirstName());
    person.setLastName(dto.getLastName());
    person.setAddress(dto.getAddress());
    person.setCity(dto.getCity());
    person.setState(dto.getState().toUpperCase());
    person.setTelephone(dto.getTelephone());
    person.setZipCode(dto.getZipcode());
    person = personRepository.save(person);

    return true;
  }
}
