package com.thomaslent.tcltravels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
