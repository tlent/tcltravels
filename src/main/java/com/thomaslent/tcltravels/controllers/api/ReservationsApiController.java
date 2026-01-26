package com.thomaslent.tcltravels.controllers.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thomaslent.tcltravels.dto.AuctionView;
import com.thomaslent.tcltravels.dto.BidForm;
import com.thomaslent.tcltravels.dto.ReservationForm;
import com.thomaslent.tcltravels.dto.ReservationView;
import com.thomaslent.tcltravels.security.UserPrincipal;
import com.thomaslent.tcltravels.services.AuctionsService;
import com.thomaslent.tcltravels.services.ReservationsService;
import com.thomaslent.tcltravels.services.ReservationsService.BidResult;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservations")
@Validated
@PreAuthorize("hasRole('CUSTOMER')")
public class ReservationsApiController {

  private final ReservationsService reservationsService;
  private final AuctionsService auctionsService;

  public ReservationsApiController(
      ReservationsService reservationsService,
      AuctionsService auctionsService) {
    this.reservationsService = reservationsService;
    this.auctionsService = auctionsService;
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> getReservations(
      @RequestParam(required = false) String status,
      Authentication authentication) {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    List<ReservationView> current = List.of();
    List<ReservationView> past = List.of();

    if (status == null || "current".equalsIgnoreCase(status)) {
      current = reservationsService.getCurrentReservations(userPrincipal.getCustomerId());
    }

    if (status == null || "past".equalsIgnoreCase(status)) {
      past = reservationsService.getPastReservations(userPrincipal.getCustomerId());
    }

    List<AuctionView> auctions = auctionsService.getAuctionHistory(userPrincipal.getCustomerId());

    Map<String, Object> response = new HashMap<>();
    response.put("current", current);
    response.put("past", past);
    response.put("auctions", auctions);

    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<?> createReservation(
      @Valid @RequestBody ReservationForm reservationForm,
      Authentication authentication) {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Optional<String> error = reservationsService.createReservation(
        reservationForm, userPrincipal.getCustomerId());

    if (error.isPresent()) {
      return ResponseEntity.badRequest()
          .body(new ErrorResponse(error.get()));
    }

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new SuccessResponse("Reservation created successfully"));
  }

  @PostMapping("/bid")
  public ResponseEntity<?> placeBid(
      @Valid @RequestBody BidForm bidForm,
      Authentication authentication) {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    BidResult result = reservationsService.submitBid(
        bidForm, userPrincipal.getCustomerId(), userPrincipal.getPersonId());

    if (result.error() != null) {
      return ResponseEntity.badRequest()
          .body(new ErrorResponse(result.error()));
    }

    if (result.accepted()) {
      return ResponseEntity.ok(new BidResponse(true, false,
          "Congratulations! Your bid was accepted and a reservation has been created."));
    }

    if (result.rejected()) {
      return ResponseEntity.ok(new BidResponse(false, true,
          "Your bid was too low and was not accepted. Please try again with a higher amount."));
    }

    return ResponseEntity.ok(new BidResponse(false, false, "Bid submitted successfully"));
  }

  private static class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
      this.error = error;
    }

    public String getError() {
      return error;
    }

    public void setError(String error) {
      this.error = error;
    }
  }

  private static class SuccessResponse {
    private String message;

    public SuccessResponse(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }

  private static class BidResponse {
    private boolean accepted;
    private boolean rejected;
    private String message;

    public BidResponse(boolean accepted, boolean rejected, String message) {
      this.accepted = accepted;
      this.rejected = rejected;
      this.message = message;
    }

    public boolean isAccepted() {
      return accepted;
    }

    public void setAccepted(boolean accepted) {
      this.accepted = accepted;
    }

    public boolean isRejected() {
      return rejected;
    }

    public void setRejected(boolean rejected) {
      this.rejected = rejected;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}
