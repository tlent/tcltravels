package com.thomaslent.tcltravels.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.thomaslent.tcltravels.dto.AirportOptionView;
import com.thomaslent.tcltravels.dto.CustomerOptionView;
import com.thomaslent.tcltravels.dto.FlightOptionView;

class AdminSelectionServiceTest {

  private final AdminSelectionService service = new AdminSelectionService();

  @Test
  void selectFlightKey_returnsProvidedKeyWhenValid() {
    String result = service.selectFlightKey("AA12", List.of(new FlightOptionView("BB", 99)));

    assertThat(result).isEqualTo("AA12");
  }

  @Test
  void selectFlightKey_fallsBackToFirstOption() {
    String result = service.selectFlightKey(null, List.of(new FlightOptionView("DL", 7)));

    assertThat(result).isEqualTo("DL7");
  }

  @Test
  void parseFlightKey_handlesShortOrNullKeys() {
    assertThat(service.parseFlightKey(null).airlineId()).isEmpty();
    assertThat(service.parseFlightKey("A1").flightNumber()).isZero();
  }

  @Test
  void selectCustomerName_returnsFullNameWhenFound() {
    List<CustomerOptionView> options = List.of(new CustomerOptionView(1L, "Ada", "Lovelace"));

    assertThat(service.selectCustomerName(1L, options)).isEqualTo("Ada Lovelace");
  }

  @Test
  void selectCityAndAirport_fallBackToFirstOption() {
    assertThat(service.selectCity("", List.of("Boston"))).isEqualTo("Boston");
    assertThat(service.selectAirportId(null, List.of(new AirportOptionView("BOS", "Boston"))))
        .isEqualTo("BOS");
  }
}
