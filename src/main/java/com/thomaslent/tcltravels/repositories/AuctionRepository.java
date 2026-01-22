package com.thomaslent.tcltravels.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thomaslent.tcltravels.entities.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
  List<Auction> findByCustomerIdOrderByDateDesc(Long customerId);
}
