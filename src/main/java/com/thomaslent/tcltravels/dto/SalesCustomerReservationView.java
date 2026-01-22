package com.thomaslent.tcltravels.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SalesCustomerReservationView(
    Long reservationNumber,
    String airlineId,
    Integer flightNumber,
    OffsetDateTime reservationDate,
    BigDecimal bookingFee,
    BigDecimal totalFare) {
}
