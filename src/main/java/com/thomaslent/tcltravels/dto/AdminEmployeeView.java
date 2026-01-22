package com.thomaslent.tcltravels.dto;

import java.math.BigDecimal;

public record AdminEmployeeView(
    Long personId,
    String firstName,
    String lastName,
    String telephone,
    Integer ssn,
    Boolean isManager,
    BigDecimal hourlyRate) {
}
