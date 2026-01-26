package com.thomaslent.tcltravels.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thomaslent.tcltravels.dto.UserDto;
import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Person;
import com.thomaslent.tcltravels.entities.User;
import com.thomaslent.tcltravels.repositories.CustomerRepository;
import com.thomaslent.tcltravels.repositories.PersonRepository;
import com.thomaslent.tcltravels.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private PersonRepository personRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @Test
  void registerNewCustomer_encodesPasswordAndCreatesEntities() {
    UserDto dto = new UserDto();
    dto.setFirstName("Jane");
    dto.setLastName("Doe");
    dto.setAddress("123 Main St");
    dto.setCity("Boston");
    dto.setState("ma");
    dto.setZipcode("02110");
    dto.setTelephone("1234567890");
    dto.setEmail("jane@example.com");
    dto.setPassword("secret");

    when(passwordEncoder.encode("secret")).thenReturn("hashed");
    when(personRepository.save(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));
    when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
    when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

    userService.registerNewCustomer(dto);

    ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

    verify(personRepository).save(personCaptor.capture());
    verify(userRepository).save(userCaptor.capture());
    verify(customerRepository).save(customerCaptor.capture());

    Person savedPerson = personCaptor.getValue();
    assertThat(savedPerson.getFirstName()).isEqualTo("Jane");
    assertThat(savedPerson.getState()).isEqualTo("MA");
    assertThat(savedPerson.getZipCode()).isEqualTo("02110");

    User savedUser = userCaptor.getValue();
    assertThat(savedUser.getUsername()).isEqualTo("jane@example.com");
    assertThat(savedUser.getPassword()).isEqualTo("hashed");
    assertThat(savedUser.getRole()).isEqualTo("CUSTOMER");
    assertThat(savedUser.getPerson()).isSameAs(savedPerson);

    Customer savedCustomer = customerCaptor.getValue();
    assertThat(savedCustomer.getAccount()).isSameAs(savedUser);
  }

  @Test
  void editCustomer_returnsFalseWhenPasswordDoesNotMatch() {
    Person person = new Person();
    User user = new User();
    user.setPassword("hashed");

    when(personRepository.findById(5L)).thenReturn(Optional.of(person));
    when(userRepository.findByPerson(person)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("bad", "hashed")).thenReturn(false);

    UserDto dto = new UserDto();
    dto.setPassword("bad");

    boolean updated = userService.editCustomer(5L, dto);

    assertThat(updated).isFalse();
    verify(personRepository, never()).save(any(Person.class));
  }

  @Test
  void editCustomer_updatesPersonWhenPasswordMatches() {
    Person person = new Person();
    User user = new User();
    user.setPassword("hashed");

    when(personRepository.findById(7L)).thenReturn(Optional.of(person));
    when(userRepository.findByPerson(person)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("good", "hashed")).thenReturn(true);
    when(personRepository.save(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));

    UserDto dto = new UserDto();
    dto.setPassword("good");
    dto.setFirstName("Jo");
    dto.setLastName("Lee");
    dto.setAddress("44 South");
    dto.setCity("Austin");
    dto.setState("tx");
    dto.setTelephone("1234567890");
    dto.setZipcode("73301");

    boolean updated = userService.editCustomer(7L, dto);

    assertThat(updated).isTrue();

    ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
    verify(personRepository).save(personCaptor.capture());

    Person savedPerson = personCaptor.getValue();
    assertThat(savedPerson.getFirstName()).isEqualTo("Jo");
    assertThat(savedPerson.getState()).isEqualTo("TX");
    assertThat(savedPerson.getZipCode()).isEqualTo("73301");
  }
}
