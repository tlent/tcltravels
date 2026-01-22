INSERT INTO
  airlines
VALUES
  ('AB', 'Air Berlin'),
  ('AJ', 'Air Japan'),
  ('AM', 'Air Madagascar'),
  ('AA', 'American Airlines'),
  ('BA', 'British Airways'),
  ('DA', 'Delta Airlines'),
  ('JB', 'JetBlue Airways'),
  ('LA', 'Lufthansa'),
  ('SA', 'Southwest Airlines'),
  ('UA', 'United Airlines');

INSERT INTO
  airports
VALUES
  ('BTA', 'Berlin Tegel', 'Berlin', 'Germany'),
  (
    'COI',
    'Chicago O''Hare International',
    'Chicago',
    'Illinois'
  ),
  (
    'HAI',
    'Hartsfield-Jackson Atlanta Int',
    'Atlanta',
    'United States of America'
  ),
  (
    'IIA',
    'Ivato International',
    'Antananarivo',
    'Madagascar'
  ),
  (
    'JFK',
    'John F. Kennedy International',
    'New York',
    'United States of America'
  ),
  (
    'LGA',
    'LaGuardia',
    'New York',
    'United States of America'
  ),
  (
    'LIA',
    'Logan International',
    'Boston',
    'United States '
  ),
  (
    'LHA',
    'London Heathrow',
    'London',
    'United Kingdom'
  ),
  (
    'LAX',
    'Los Angeles International',
    'Los Angeles',
    'United States of America'
  ),
  (
    'SFI',
    'San Francisco International',
    'San Francisco',
    'United States of America'
  ),
  ('TIA', 'Tokyo International', 'Tokyo', 'Japan');

INSERT INTO
  persons
VALUES
  (
    1,
    'John',
    'Doe',
    '123 N Fake Street, New York, New York 10001',
    'New York',
    'NY',
    10001,
    '1231231234'
  ),
  (
    2,
    'Jane',
    'Smith',
    '100 Nicolls Rd, Stony Brook, New York 17790',
    'Stony Brook',
    'NY',
    17790,
    '5555555555'
  ),
  (
    3,
    'Rick',
    'Astley',
    '1337 Internet Lane, Los Angeles, California 90001',
    'Los Angeles',
    'CA',
    90001,
    '3141592653'
  ),
  (
    4,
    'Bob',
    'Ross',
    '100 Main St, New York, New York 11111',
    'New York',
    'NY',
    11111,
    '1234567890'
  ),
  (
    5,
    'BestCustomerRep',
    'GiveMeARaise',
    'Home',
    'New York',
    'NY',
    11790,
    '5555555555'
  ),
  (
    6,
    'Lynard',
    'Skynard',
    'SomeWhere',
    'Else',
    'NY',
    12345,
    '1234567899'
  );

INSERT INTO
  app_users (id, username, password, role, person_id)
VALUES
  (1, 'jdoe@woot.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5lu4bdpDT6vKrrfE0YyV1ypE1U9m6', 'CUSTOMER', 1),
  (2, 'awesomejane@ftw.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5lu4bdpDT6vKrrfE0YyV1ypE1U9m6', 'CUSTOMER', 2),
  (3, 'rickroller@rolld.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5lu4bdpDT6vKrrfE0YyV1ypE1U9m6', 'CUSTOMER', 3),
  (4, 'bross@email.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5lu4bdpDT6vKrrfE0YyV1ypE1U9m6', 'EMPLOYEE', 4),
  (5, 'best@customerreps.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5lu4bdpDT6vKrrfE0YyV1ypE1U9m6', 'EMPLOYEE', 5),
  (6, 'skynyrd03@freebird.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5lu4bdpDT6vKrrfE0YyV1ypE1U9m6', 'MANAGER', 6);

INSERT INTO
  customers (id, account_id, creation_date, rating)
VALUES
  (1, 1, '2011-04-01 16:00:00', NULL),
  (2, 2, '2011-01-04 07:00:00', NULL),
  (3, 3, '2011-01-01 00:00:00', NULL);

INSERT INTO
  employees (id, account_id, ssn, start_date, hourly_rate)
VALUES
  (1, 4, 123456789, '1970-01-01', 50.00),
  (2, 5, 111111111, '1970-01-01', 1.00),
  (3, 6, 123654132, '2014-05-08', 150.00);

INSERT INTO
  flights (id, airline_id, flight_number, number_of_seats, days_operating, min_length_stay, max_length_stay,
           origin_airport_id, destination_airport_id)
VALUES
  (1, 'AA', 111, 100, 1010100, NULL, NULL, 'LGA', 'TIA'),
  (2, 'JB', 111, 150, 1111111, NULL, NULL, 'SFI', 'LHA'),
  (3, 'AM', 1337, 33, 0000011, NULL, NULL, 'JFK', 'IIA'),
  (4, 'JB', 777, 150, 1111111, NULL, NULL, 'SFI', 'LHA'),
  (5, 'JB', 909, 120, 1111111, NULL, NULL, 'SFI', 'LHA');

-- 0 = One way
-- 1 = Round trip
-- 2 = Hidden fare (for reverse auctions)
INSERT INTO
  fares (flight_id, fare_type, class, fare)
VALUES
  (1, 0, 'First', 100.00),
  (1, 1, 'First', 190.00),
  (1, 2, 'First', 80.00),
  (1, 0, 'Business', 50.00),
  (1, 1, 'Business', 80.00),
  (1, 2, 'Business', 30.00),
  (1, 0, 'Economy', 30.00),
  (1, 1, 'Economy', 60.00),
  (1, 2, 'Economy', 10.00),
  (2, 0, 'First', 150.00),
  (2, 1, 'First', 250.00),
  (2, 2, 'First', 140.00),
  (2, 0, 'Business', 100.00),
  (2, 1, 'Business', 170.00),
  (2, 2, 'Business', 90.00),
  (2, 0, 'Economy', 80.00),
  (2, 1, 'Economy', 150.00),
  (2, 2, 'Economy', 70.00),
  (3, 0, 'First', 100.00),
  (3, 1, 'First', 150.00),
  (3, 2, 'First', 95.00),
  (3, 0, 'Business', 75.00),
  (3, 1, 'Business', 150.00),
  (3, 2, 'Business', 50.00),
  (3, 0, 'Economy', 55.00),
  (3, 1, 'Economy', 130.00),
  (3, 2, 'Economy', 30.00),
  (4, 0, 'First', 150.00),
  (4, 1, 'First', 300.00),
  (4, 2, 'First', 125.00),
  (4, 0, 'Business', 125.00),
  (4, 1, 'Business', 200.00),
  (4, 2, 'Business', 100.00),
  (4, 0, 'Economy', 105.00),
  (4, 1, 'Economy', 180.00),
  (4, 2, 'Economy', 80.00),
  (5, 0, 'First', 200.00),
  (5, 1, 'First', 350.00),
  (5, 2, 'First', 180.00),
  (5, 0, 'Business', 150.00),
  (5, 1, 'Business', 275.00),
  (5, 2, 'Business', 130.00),
  (5, 0, 'Economy', 95.00),
  (5, 1, 'Economy', 175.00),
  (5, 2, 'Economy', 85.00);

INSERT INTO
  advance_purchase_discounts (airline_id, days, discount_rate)
VALUES
  ('JB', 7, 10),
  ('AA', 14, 15);

INSERT INTO
  stops_at (flight_id, stop_number, airport_id, arrival_time, departure_time, arrival_delay, departure_delay)
VALUES
  (1, 1, 'LGA', '2011-01-05 09:00:00', '2011-01-05 11:00:00', 0, 0),
  (1, 2, 'LAX', '2011-01-05 17:00:00', '2011-01-05 19:00:00', 0, 0),
  (1, 3, 'TIA', '2011-01-06 07:30:00', '2011-01-06 10:00:00', 0, 0),
  (2, 1, 'SFI', '2011-01-10 12:00:00', '2011-01-10 14:00:00', 0, 0),
  (2, 2, 'LIA', '2011-01-10 19:30:00', '2011-01-10 22:30:00', 0, 0),
  (2, 3, 'LHA', '2011-01-11 05:00:00', '2011-01-11 08:00:00', 0, 0),
  (3, 1, 'JFK', '2011-01-13 05:00:00', '2011-01-13 07:00:00', 0, 0),
  (3, 2, 'IIA', '2011-01-13 23:00:00', '2011-01-14 03:00:00', 0, 0),
  (4, 1, 'SFI', '2015-01-01 00:00:00', '2015-01-01 02:00:00', 0, 0),
  (4, 2, 'JFK', '2015-01-01 03:00:00', '2015-01-01 05:00:00', 0, 0),
  (4, 3, 'LHA', '2015-01-01 07:00:00', '2015-01-01 10:00:00', 0, 0),
  (5, 1, 'SFI', '2030-06-01 08:00:00', '2030-06-01 10:00:00', 0, 0),
  (5, 2, 'LIA', '2030-06-01 16:00:00', '2030-06-01 18:00:00', 0, 0),
  (5, 3, 'LHA', '2030-06-02 02:00:00', '2030-06-02 05:00:00', 0, 0);

INSERT INTO
  reservations (id, reservation_date, booking_fee, total_fare, employee_id, customer_id)
VALUES
  (111, '2011-01-01 06:00:00', 30.00, 1200.00, 1, 1),
  (222, '2011-01-01 00:00:00', 30.00, 500.00, 1, 2),
  (333, '2011-01-01 00:00:00', 30.00, 3333.33, 1, 3),
  (444, '2014-01-01 00:00:00', 30.00, 500.00, 1, 1),
  (999, '2011-01-01 00:00:00', 30.00, 999999.00, 2, 1),
  (911, NOW(), 30.00, 77777.00, 2, 3),
  (977, NOW(), 30.00, 150.00, 2, 3);

INSERT INTO
  legs (reservation_id, leg_number, flight_id, from_stop_number)
VALUES
  (111, 1, 1, 1),
  (111, 2, 1, 2),
  (222, 1, 2, 2),
  (333, 1, 3, 1),
  (444, 1, 2, 1),
  (999, 1, 2, 1),
  (911, 1, 4, 1),
  (977, 1, 4, 2);

INSERT INTO
  passengers (id, customer_id, passenger_name)
VALUES
  (1, 1, 'John Doe'),
  (2, 2, 'Jane Smith'),
  (3, 3, 'Rick Astley'),
  (4, 1, 'Additional Passenger');

INSERT INTO
  reservation_passengers (reservation_id, passenger_id, seat_number, class, meal)
VALUES
  (111, 1, 1, 'Economy', 'Chips'),
  (222, 2, 1, 'First', 'Fish and Chips'),
  (333, 3, 1, 'First', 'Sushi'),
  (111, 4, 2, 'Economy', 'Chips'),
  (444, 1, 1, 'Business', 'Chips'),
  (999, 1, 1, 'Economy', 'Steak'),
  (911, 3, 1, 'First', 'Chips'),
  (977, 3, 1, 'Economy', 'Chips');

INSERT INTO
  auctions (id, customer_id, flight_id, class, date, name_your_own_price, accepted)
VALUES
  (1, 1, 1, 'Economy', '2011-01-01', 400.00, true),
  (2, 2, 1, 'Economy', '2011-01-01', 500.00, true);

SELECT
  setval(
    pg_get_serial_sequence('persons', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        persons
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('app_users', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        app_users
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('customers', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        customers
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('employees', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        employees
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('flights', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        flights
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('stops_at', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        stops_at
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('fares', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        fares
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('advance_purchase_discounts', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        advance_purchase_discounts
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('reservations', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        reservations
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('legs', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        legs
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('passengers', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        passengers
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('reservation_passengers', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        reservation_passengers
    )
  );

SELECT
  setval(
    pg_get_serial_sequence('auctions', 'id'),
    (
      SELECT
        MAX(id)
      FROM
        auctions
    )
  );
