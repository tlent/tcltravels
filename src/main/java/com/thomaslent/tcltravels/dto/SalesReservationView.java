package com.thomaslent.tcltravels.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SalesReservationView(
    Long reservationNumber,
    String customerName,
    OffsetDateTime reservationDate,
    BigDecimal bookingFee,
    BigDecimal totalFare) {
}
