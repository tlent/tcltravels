package com.thomaslent.tcltravels.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaslent.tcltravels.entities.Customer;
import com.thomaslent.tcltravels.entities.Person;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  public Optional<Customer> findByPerson(Person person);

  List<Customer> findAllByOrderByPersonLastNameAscPersonFirstNameAsc();
}
