package com.thomaslent.tcltravels.dto;

import java.time.OffsetDateTime;

public record CustomerOnFlightView(
    Long reservationNumber,
    Long accountNumber,
    String passengerName,
    String seatClass,
    Long passengerCount,
    OffsetDateTime reservationDate) {
}
