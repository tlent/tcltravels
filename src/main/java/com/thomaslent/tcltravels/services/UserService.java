package com.thomaslent.tcltravels.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Person;
import com.thomaslent.tcltravels.entities.User;
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
    Person person = personRepository.findById(id).get();
    Customer customer = customerRepository.findByPerson(person).get();
    User user = userRepository.findByPerson(person).get();

    UserDto userDto = new UserDto();
    userDto.setFirstName(person.getFirstName());
    userDto.setLastName(person.getLastName());
    userDto.setAddress(person.getAddress());
    userDto.setCity(person.getCity());
    userDto.setState(person.getState());
    userDto.setZipcode(person.getZipCode().toString());
    userDto.setTelephone(person.getTelephone());
    userDto.setCreditCardNumber(customer.getCreditCardNumber());
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
    person.setZipCode(Integer.parseInt(dto.getZipcode()));
    person = personRepository.save(person);

    User user = new User();
    user.setUsername(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setPerson(person);
    userRepository.save(user);

    Customer customer = new Customer();
    customer.setCreditCardNumber(dto.getCreditCardNumber());
    customer.setEmail(dto.getEmail());
    customer.setPerson(person);
    customerRepository.save(customer);
  }

  @Transactional
  public boolean editCustomer(Long id, UserDto dto) {
    Person person = personRepository.findById(id).get();
    User user = userRepository.findByPerson(person).get();
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
    person.setZipCode(Integer.parseInt(dto.getZipcode()));
    person = personRepository.save(person);

    Customer customer = customerRepository.findByPerson(person).get();
    customer.setCreditCardNumber(dto.getCreditCardNumber());
    customerRepository.save(customer);
    return true;
  }
}
