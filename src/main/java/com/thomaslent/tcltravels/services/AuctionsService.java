package com.thomaslent.tcltravels.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.thomaslent.tcltravels.dto.AuctionView;
import com.thomaslent.tcltravels.entities.Auction;
import com.thomaslent.tcltravels.repositories.AuctionRepository;

@Service
public class AuctionsService {
  private AuctionRepository auctionRepository;

  public AuctionsService(AuctionRepository auctionRepository) {
    this.auctionRepository = auctionRepository;
  }

  public List<AuctionView> getAuctionHistory(Long customerId) {
    List<Auction> auctions = auctionRepository.findByCustomerIdOrderByDateDesc(customerId);
    return auctions.stream()
        .map(auction -> new AuctionView(
            auction.getFlight().getAirline().getId(),
            auction.getFlight().getFlightNumber(),
            auction.getSeatingClass(),
            auction.getDate(), auction.getNameYourOwnPrice(), auction.getAccepted()))
        .collect(Collectors.toList());
  }
}
