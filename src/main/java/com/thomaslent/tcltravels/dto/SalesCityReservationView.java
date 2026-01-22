package com.thomaslent.tcltravels.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SalesCityReservationView(
    Long reservationNumber,
    String airlineId,
    Integer flightNumber,
    OffsetDateTime reservationDate,
    BigDecimal bookingFee,
    BigDecimal totalFare) {
}
