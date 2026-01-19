package com.thomaslent.tcltravels.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaslent.tcltravels.entities.Employee;
import com.thomaslent.tcltravels.entities.Person;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  public Optional<Employee> findByPerson(Person person);
}
