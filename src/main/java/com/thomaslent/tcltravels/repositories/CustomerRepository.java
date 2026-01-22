package com.thomaslent.tcltravels.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaslent.tcltravels.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByAccountId(Long accountId);

  Optional<Customer> findByAccountPersonId(Long personId);

  List<Customer> findAllByOrderByAccountPersonLastNameAscAccountPersonFirstNameAsc();
}
