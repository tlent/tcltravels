package com.thomaslent.tcltravels.entities;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stops_at")
public class StopsAt {
  @EmbeddedId
  private StopsAtId id;

  @ManyToOne
  @JoinColumn(name = "airport_id", referencedColumnName = "id")
  private Airport airport;

  @Column(name = "arrival_time")
  private OffsetDateTime arrivalTime;

  @Column(name = "departure_time")
  private OffsetDateTime departureTime;

  @Column(name = "arrival_delay")
  private Integer arrivalDelay;

  @Column(name = "departure_delay")
  private Integer departureDelay;

  public StopsAtId getId() {
    return id;
  }

  public void setId(StopsAtId id) {
    this.id = id;
  }

  public Airport getAirport() {
    return airport;
  }

  public void setAirport(Airport airport) {
    this.airport = airport;
  }

  public OffsetDateTime getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(OffsetDateTime arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public OffsetDateTime getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(OffsetDateTime departureTime) {
    this.departureTime = departureTime;
  }

  public Integer getArrivalDelay() {
    return arrivalDelay;
  }

  public void setArrivalDelay(Integer arrivalDelay) {
    this.arrivalDelay = arrivalDelay;
  }

  public Integer getDepartureDelay() {
    return departureDelay;
  }

  public void setDepartureDelay(Integer departureDelay) {
    this.departureDelay = departureDelay;
  }

}
