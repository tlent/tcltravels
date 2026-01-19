package com.thomaslent.tcltravels.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thomaslent.tcltravels.dto.RegistrationDto;
import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Person;
import com.thomaslent.tcltravels.entities.User;
import com.thomaslent.tcltravels.repositories.CustomerRepository;
import com.thomaslent.tcltravels.repositories.PersonRepository;
import com.thomaslent.tcltravels.repositories.UserRepository;

@Service
public class RegistrationService {

  private PersonRepository personRepository;
  private UserRepository userRepository;
  private CustomerRepository customerRepository;

  public RegistrationService(PersonRepository personRepository, UserRepository userRepository,
      CustomerRepository customerRepository) {
    this.personRepository = personRepository;
    this.userRepository = userRepository;
    this.customerRepository = customerRepository;
  }

  @Transactional
  public void registerNewCustomer(RegistrationDto dto) {
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
    user.setPassword(dto.getPassword());
    user.setPerson(person);
    userRepository.save(user);

    Customer customer = new Customer();
    customer.setCreditCardNumber(dto.getCreditCard());
    customer.setEmail(dto.getEmail());
    customer.setPerson(person);
    customerRepository.save(customer);
  }
}
