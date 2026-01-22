package com.thomaslent.tcltravels.dto;

import java.math.BigDecimal;

public record TopRevenueView(
    String firstName,
    String lastName,
    BigDecimal totalRevenue) {
}
