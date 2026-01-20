package com.thomaslent.tcltravels.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AuctionView(
    String airlineId,
    Integer flightNumber,
    String seatingClass,
    LocalDate date,
    BigDecimal nameYourOwnPrice,
    Boolean accepted) {
}
