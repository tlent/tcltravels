package com.thomaslent.tcltravels.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thomaslent.tcltravels.entities.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
        @Query(value = """
                        SELECT DISTINCT r.*
                        FROM reservations r
                        LEFT JOIN legs l
                        ON r.reservation_number = l.reservation_number
                        JOIN stops_at s
                        ON s.airline_id = l.airline_id
                        AND s.flight_number = l.flight_number
                        AND s.stop_number = l.from_stop_number
                        WHERE s.departure_time >= NOW()
                        AND r.account_number = :accountNumber
                        ORDER BY r.reservation_date DESC
                        """, nativeQuery = true)
        public List<Reservation> findCurrentReservations(@Param("accountNumber") Long accountNumber);

        @Query(value = """
                        SELECT DISTINCT r.*
                        FROM reservations r
                        LEFT JOIN legs l
                        ON r.reservation_number = l.reservation_number
                        JOIN stops_at s
                        ON s.airline_id = l.airline_id
                        AND s.flight_number = l.flight_number
                        AND s.stop_number = l.from_stop_number
                        WHERE s.departure_time < NOW()
                        AND r.account_number = :accountNumber
                        ORDER BY r.reservation_date DESC
                        """, nativeQuery = true)
        public List<Reservation> findPastReservations(@Param("accountNumber") Long accountNumber);
}
