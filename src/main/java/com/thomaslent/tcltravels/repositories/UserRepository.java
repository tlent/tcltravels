package com.thomaslent.tcltravels.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaslent.tcltravels.entities.Person;
import com.thomaslent.tcltravels.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsernameAndPassword(String username, String password);

  public Optional<User> findByPerson(Person person);

  boolean existsByUsername(String username);
}
