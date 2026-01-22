package com.thomaslent.tcltravels.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "legs")
public class Leg {
  @EmbeddedId
  private LegId id;

  @ManyToOne
  @MapsId("reservationNumber")
  @JoinColumn(name = "reservation_number", referencedColumnName = "reservation_number")
  private Reservation reservation;

  @ManyToOne
  @JoinColumns({
      @JoinColumn(name = "airline_id", referencedColumnName = "airline_id"),
      @JoinColumn(name = "flight_number", referencedColumnName = "flight_number")
  })
  private Flight flight;

  @ManyToOne
  @JoinColumns({
      @JoinColumn(name = "airline_id", referencedColumnName = "airline_id", insertable = false, updatable = false),
      @JoinColumn(name = "flight_number", referencedColumnName = "flight_number", insertable = false, updatable = false),
      @JoinColumn(name = "from_stop_number", referencedColumnName = "stop_number", insertable = false, updatable = false)
  })
  private StopsAt fromStop;

  @Column(name = "from_stop_number")
  private Integer fromStopNumber;

  public LegId getId() {
    return id;
  }

  public void setId(LegId id) {
    this.id = id;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

  public StopsAt getFromStop() {
    return fromStop;
  }

  public void setFromStop(StopsAt fromStop) {
    this.fromStop = fromStop;
  }

  public Integer getFromStopNumber() {
    return fromStopNumber;
  }

  public void setFromStopNumber(Integer fromStopNumber) {
    this.fromStopNumber = fromStopNumber;
  }
}
