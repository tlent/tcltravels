package com.thomaslent.tcltravels.repositories;

import java.util.List;
import java.util.Optional;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.entities.Employee;
import com.thomaslent.tcltravels.entities.Person;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  public Optional<Employee> findByPerson(Person person);

  List<Employee> findAllByOrderByPersonLastNameAscPersonFirstNameAsc();

  @Modifying
  @Query(value = """
      update employees
      set ssn = :ssn,
          is_manager = :isManager,
          start_date = :startDate,
          hourly_rate = :hourlyRate
      where id = :personId
      """, nativeQuery = true)
  int updateEmployeeByPersonId(
      @Param("personId") Long personId,
      @Param("ssn") Integer ssn,
      @Param("isManager") Boolean isManager,
      @Param("startDate") LocalDate startDate,
      @Param("hourlyRate") java.math.BigDecimal hourlyRate);
}
