package com.thomaslent.tcltravels.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReservationView(
    Long reservationNumber,
    Long accountNumber,
    OffsetDateTime reservationDate,
    BigDecimal bookingFee,
    BigDecimal totalFare,
    List<LegView> legs) {
}
