package com.thomaslent.tcltravels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaslent.tcltravels.entities.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}