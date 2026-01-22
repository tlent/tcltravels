package com.thomaslent.tcltravels.repositories;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.dto.SalesCityReservationView;
import com.thomaslent.tcltravels.dto.TopRevenueView;
import com.thomaslent.tcltravels.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
        @Query(value = """
                        SELECT DISTINCT r.*
                        FROM reservations r
                        LEFT JOIN legs l
                        ON r.id = l.reservation_id
                        JOIN stops_at s
                        ON s.flight_id = l.flight_id
                        AND s.stop_number = l.from_stop_number
                        WHERE s.departure_time >= NOW()
                        AND r.customer_id = :customerId
                        ORDER BY r.reservation_date DESC
                        """, nativeQuery = true)
        public List<Reservation> findCurrentReservations(@Param("customerId") Long customerId);

        @Query(value = """
                        SELECT DISTINCT r.*
                        FROM reservations r
                        LEFT JOIN legs l
                        ON r.id = l.reservation_id
                        JOIN stops_at s
                        ON s.flight_id = l.flight_id
                        AND s.stop_number = l.from_stop_number
                        WHERE s.departure_time < NOW()
                        AND r.customer_id = :customerId
                        ORDER BY r.reservation_date DESC
                        """, nativeQuery = true)
        public List<Reservation> findPastReservations(@Param("customerId") Long customerId);

        List<Reservation> findByReservationDateBetweenOrderByTotalFareDesc(
                        OffsetDateTime start, OffsetDateTime end);

        @Query("""
                        select sum(r.totalFare)
                        from Reservation r
                        where r.reservationDate between :start and :end
                        """)
        BigDecimal sumTotalFareByReservationDateBetween(@Param("start") OffsetDateTime start,
                        @Param("end") OffsetDateTime end);

        List<Reservation> findByCustomerIdOrderByTotalFareDesc(Long customerId);

        @Query("""
                        select sum(r.totalFare)
                        from Reservation r
                        where r.customer.id = :customerId
                        """)
        BigDecimal sumTotalFareByCustomerId(@Param("customerId") Long customerId);

        @Query("""
                        select distinct r
                        from Reservation r
                        join Leg l on l.reservation = r
                        where l.flight.airline.id = :airlineId
                        and l.flight.flightNumber = :flightNumber
                        order by r.totalFare desc
                        """)
        List<Reservation> findByFlight(@Param("airlineId") String airlineId,
                        @Param("flightNumber") Integer flightNumber);

        @Query("""
                        select sum(r.totalFare)
                        from Reservation r
                        join Leg l on l.reservation = r
                        where l.flight.airline.id = :airlineId
                        and l.flight.flightNumber = :flightNumber
                        """)
        BigDecimal sumTotalFareByFlight(@Param("airlineId") String airlineId,
                        @Param("flightNumber") Integer flightNumber);

        @Query("""
                        select new com.thomaslent.tcltravels.dto.TopRevenueView(
                          p.firstName,
                          p.lastName,
                          sum(r.totalFare)
                        )
                        from Reservation r
                        join r.customer c
                        join c.account a
                        join a.person p
                        group by p.id, p.firstName, p.lastName
                        order by sum(r.totalFare) desc
                        """)
        List<TopRevenueView> findTopCustomers(Pageable pageable);

        @Query("""
                        select new com.thomaslent.tcltravels.dto.TopRevenueView(
                          p.firstName,
                          p.lastName,
                          sum(r.totalFare)
                        )
                        from Reservation r
                        join r.employee e
                        join e.account a
                        join a.person p
                        where a.role = 'EMPLOYEE'
                        group by p.id, p.firstName, p.lastName
                        order by sum(r.totalFare) desc
                        """)
        List<TopRevenueView> findTopEmployees(Pageable pageable);

        @Query("""
                        select distinct new com.thomaslent.tcltravels.dto.SalesCityReservationView(
                          r.id,
                          l.flight.airline.id,
                          l.flight.flightNumber,
                          r.reservationDate,
                          r.bookingFee,
                          r.totalFare
                        )
                        from Reservation r
                        join Leg l on l.reservation = r
                        join StopsAt s on s.flight = l.flight
                          and s.stopNumber = l.fromStopNumber + 1
                        join s.airport a
                        where a.city = :city
                        order by r.totalFare desc
                        """)
        List<SalesCityReservationView> findReservationsByDestinationCity(@Param("city") String city);
}
