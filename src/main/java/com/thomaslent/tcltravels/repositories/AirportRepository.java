package com.thomaslent.tcltravels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.Airport;

public interface AirportRepository extends JpaRepository<Airport, String> {

}
