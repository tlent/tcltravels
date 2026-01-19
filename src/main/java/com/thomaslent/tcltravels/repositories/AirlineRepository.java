package com.thomaslent.tcltravels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.Airline;

public interface AirlineRepository extends JpaRepository<Airline, String> {

}
