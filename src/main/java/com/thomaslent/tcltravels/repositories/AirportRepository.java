package com.thomaslent.tcltravels.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thomaslent.tcltravels.entities.Airport;

public interface AirportRepository extends JpaRepository<Airport, String> {
  List<Airport> findAllByOrderByIdAsc();

  @Query("select distinct a.city from Airport a order by a.city")
  List<String> findDistinctCities();
}
