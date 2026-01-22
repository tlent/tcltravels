package com.thomaslent.tcltravels.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FlightScheduleService {
  private static final String[] DAY_LABELS = { "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su" };

  public String formatDaysOperating(String daysOperating) {
    if ("1111111".equals(daysOperating)) {
      return "Every Day";
    }
    if ("0000000".equals(daysOperating)) {
      return "Not Active";
    }
    if (daysOperating == null || daysOperating.length() != 7) {
      return "";
    }

    List<String> days = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      if (daysOperating.charAt(i) == '1') {
        days.add(DAY_LABELS[i]);
      }
    }
    return String.join("-", days);
  }
}
