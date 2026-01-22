package com.thomaslent.tcltravels.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.Airport;

public interface AirportRepository extends JpaRepository<Airport, String> {
  List<Airport> findAllByOrderByIdAsc();

  List<String> findDistinctCityByOrderByCityAsc();
}
