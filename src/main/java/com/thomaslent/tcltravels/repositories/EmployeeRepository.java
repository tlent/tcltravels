package com.thomaslent.tcltravels.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaslent.tcltravels.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  Optional<Employee> findByAccountId(Long accountId);

  Optional<Employee> findByAccountPersonId(Long personId);

  List<Employee> findAllByOrderByAccountPersonLastNameAscAccountPersonFirstNameAsc();
}
