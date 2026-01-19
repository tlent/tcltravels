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
  flights
VALUES
  ('AA', 111, 100, 1010100, NULL, NULL),
  ('JB', 111, 150, 1111111, NULL, NULL),
  ('AM', 1337, 33, 0000011, NULL, NULL);

-- 0 = One way
-- 1 = Round trip
-- 2 = Hidden fare (for reverse auctions)
INSERT INTO
  fares
VALUES
  ('AA', 111, 0, 'First', 100.00),
  ('AA', 111, 1, 'First', 190.00),
  ('AA', 111, 2, 'First', 80.00),
  ('AA', 111, 0, 'Business', 50.00),
  ('AA', 111, 1, 'Business', 80.00),
  ('AA', 111, 2, 'Business', 30.00),
  ('AA', 111, 0, 'Economy', 30.00),
  ('AA', 111, 1, 'Economy', 60.00),
  ('AA', 111, 2, 'Economy', 10.00),
  ('JB', 111, 0, 'First', 150.00),
  ('JB', 111, 1, 'First', 250.00),
  ('JB', 111, 2, 'First', 140.00),
  ('JB', 111, 0, 'Business', 100.00),
  ('JB', 111, 1, 'Business', 170.00),
  ('JB', 111, 2, 'Business', 90.00),
  ('JB', 111, 0, 'Economy', 80.00),
  ('JB', 111, 1, 'Economy', 150.00),
  ('JB', 111, 2, 'Economy', 70.00),
  ('AM', 1337, 0, 'First', 100.00),
  ('AM', 1337, 1, 'First', 150.00),
  ('AM', 1337, 2, 'First', 95.00),
  ('AM', 1337, 0, 'Business', 75.00),
  ('AM', 1337, 1, 'Business', 150.00),
  ('AM', 1337, 2, 'Business', 50.00),
  ('AM', 1337, 0, 'Economy', 55.00),
  ('AM', 1337, 1, 'Economy', 130.00),
  ('AM', 1337, 2, 'Economy', 30.00);

INSERT INTO
  stops_at
VALUES
  (
    'AA',
    111,
    1,
    'LGA',
    '2011-01-05 09:00:00',
    '2011-01-05 11:00:00',
    0,
    0
  ),
  (
    'AA',
    111,
    2,
    'LAX',
    '2011-01-05 17:00:00',
    '2011-01-05 19:00:00',
    0,
    0
  ),
  (
    'AA',
    111,
    3,
    'TIA',
    '2011-01-06 07:30:00',
    '2011-01-06 10:00:00',
    0,
    0
  ),
  (
    'JB',
    111,
    1,
    'SFI',
    '2011-01-10 12:00:00',
    '2011-01-10 14:00:00',
    0,
    0
  ),
  (
    'JB',
    111,
    2,
    'LIA',
    '2011-01-10 19:30:00',
    '2011-01-10 22:30:00',
    0,
    0
  ),
  (
    'JB',
    111,
    3,
    'LHA',
    '2011-01-11 05:00:00',
    '2011-01-11 08:00:00',
    0,
    0
  ),
  (
    'AM',
    1337,
    1,
    'JFK',
    '2011-01-13 05:00:00',
    '2011-01-13 07:00:00',
    0,
    0
  ),
  (
    'AM',
    1337,
    2,
    'IIA',
    '2011-01-13 23:00:00',
    '2011-01-14 03:00:00',
    0,
    0
  );

INSERT INTO
  persons
VALUES
  (
    1,
    'John',
    'Doe',
    '123 N Fake Street, New York, New York 10001',
    'New York',
    'New York',
    10001,
    '1231231234'
  ),
  (
    2,
    'Jane',
    'Smith',
    '100 Nicolls Rd, Stony Brook, New York 17790',
    'Stony Brook',
    'New York',
    17790,
    '5555555555'
  ),
  (
    3,
    'Rick',
    'Astley',
    '1337 Internet Lane, Los Angeles, California 90001',
    'Los Angeles',
    'California',
    90001,
    '3141592653'
  ),
  (
    4,
    'Bob',
    'Ross',
    '100 Main St, New York, New York 11111',
    'New York',
    'New York',
    11111,
    '1234567890'
  );

INSERT INTO
  employees
VALUES
  (
    4,
    123456789,
    false,
    '1970-01-01 00:00:00',
    50.00
  );

INSERT INTO
  customers
VALUES
  (
    1,
    1,
    '6666666666666666',
    'jdoe@woot.com',
    '2011-04-01 16:00:00',
    NULL
  ),
  (
    2,
    2,
    '5555555555555555',
    'awesomejane@ftw.com',
    '2011-01-04 07:00:00',
    NULL
  ),
  (
    3,
    3,
    '1111111111111111',
    'rickroller@rolld.com',
    '2011-01-01 00:00:00',
    NULL
  );

INSERT INTO
  passengers
VALUES
  (1, 1, 'John Doe'),
  (2, 2, 'Jane Smith'),
  (3, 3, 'Rick Astley');

INSERT INTO
  reservations
VALUES
  (
    111,
    '2011-01-01 06:00:00',
    30.00,
    1200.00,
    123456789,
    1
  ),
  (
    222,
    '2011-01-01 00:00:00',
    30.00,
    500.00,
    123456789,
    2
  ),
  (
    333,
    '2011-01-01 00:00:00',
    30.00,
    3333.33,
    123456789,
    3
  );

INSERT INTO
  legs
VALUES
  (111, 1, 'AA', 111, 1),
  (111, 2, 'AA', 111, 2),
  (222, 1, 'JB', 111, 2),
  (333, 1, 'AM', 1337, 1);

INSERT INTO
  reservation_passengers
VALUES
  (111, 1, 1, 1, 'Economy', 'Chips'),
  (222, 2, 2, 1, 'First', 'Fish and Chips'),
  (333, 3, 3, 1, 'First', 'Sushi');

INSERT INTO
  auctions
VALUES
  (
    1,
    1,
    'AA',
    111,
    'Economy',
    '2011-01-01',
    400.00,
    true
  );

INSERT INTO
  app_users (username, password, person_id)
VALUES
  ('rickroller@rolld.com', 'password', 3),
  ('awesomejane@ftw.com', 'password', 2),
  ('jdoe@woot.com', 'password', 1);

INSERT INTO
  passengers
VALUES
  (4, 1, 'Additional Passenger');

INSERT INTO
  reservation_passengers
VALUES
  (111, 2, 1, 1, 'Economy', 'Chips');

-- For best_selling_flights.sql
-- need another reservation for an existing flight to see
-- that it appears before others
INSERT INTO
  reservations
VALUES
  (
    444,
    '2014-01-01 00:00:00',
    30.00,
    500.00,
    123456789,
    1
  );

INSERT INTO
  reservation_passengers
VALUES
  (444, 1, 1, 1, 'Business', 'Chips');

INSERT INTO
  legs
VALUES
  (444, 1, 'JB', 111, 1);

-- For bids_history
-- adding another bid on the same auction to ensure it shows all
-- bids in the correct order
INSERT INTO
  auctions
VALUES
  (
    2,
    2,
    'AA',
    '111',
    'Economy',
    '2011-01-01',
    500,
    true
  );

-- For customer_rep_most_revenue
-- add another customer rep and a reservation making him have the most revenue
INSERT INTO
  persons
VALUES
  (
    5,
    'BestCustomerRep',
    'GiveMeARaise',
    'Home',
    'New York',
    'NY',
    11790,
    5555555555
  );

INSERT INTO
  employees
VALUES
  (5, 111111111, false, '1970-01-01 00:00:00', 1);

INSERT INTO
  reservations
VALUES
  (
    999,
    '2011-01-01 00:00:00',
    30.00,
    999999.00,
    111111111,
    1
  );

INSERT INTO
  reservation_passengers
VALUES
  (999, 1, 1, 1, 'Economy', 'Steak');

INSERT INTO
  legs
VALUES
  (999, 1, 'JB', 111, 1);

-- For revenue_by_destination_city
-- Checking that reservations that end at a leg that isn't the end of the total
-- flight have their last stop as their destination city.
-- The below  should make New York have a revenue of 77777 and not change
-- London's revenue.
INSERT INTO
  flights
VALUES
  ('JB', 777, 150, 1111111, NULL, NULL);

INSERT INTO
  fares
VALUES
  ('JB', 777, 0, 'First', 150.00),
  ('JB', 777, 1, 'First', 300.00),
  ('JB', 777, 2, 'First', 125.00),
  ('JB', 777, 0, 'Business', 125.00),
  ('JB', 777, 1, 'Business', 200.00),
  ('JB', 777, 2, 'Business', 100.00),
  ('JB', 777, 0, 'Economy', 105.00),
  ('JB', 777, 1, 'Economy', 180.00),
  ('JB', 777, 2, 'Economy', 80.00);

INSERT INTO
  advance_purchase_discounts
VALUES
  ('JB', 7, 10),
  ('AA', 14, 15);

INSERT INTO
  stops_at
VALUES
  (
    'JB',
    777,
    1,
    'SFI',
    '2015-01-01 00:00:00',
    '2015-01-01 02:00:00',
    0,
    0
  ),
  (
    'JB',
    777,
    2,
    'JFK',
    '2015-01-01 03:00:00',
    '2015-01-01 05:00:00',
    0,
    0
  ),
  (
    'JB',
    777,
    3,
    'LHA',
    '2015-01-01 07:00:00',
    '2015-01-01 10:00:00',
    0,
    0
  );

INSERT INTO
  reservations
VALUES
  (911, NOW(), 30, 77777, 111111111, 3);

INSERT INTO
  reservation_passengers
VALUES
  (911, 3, 3, 1, 'First', 'Chips');

INSERT INTO
  legs
VALUES
  (911, 1, 'JB', 777, 1);

-- Checking that revenue by destination city works correctly with
-- multiple flights ending at the same city. London should have
-- two reservations.
INSERT INTO
  reservations
VALUES
  (977, NOW(), 30, 150, 111111111, 3);

INSERT INTO
  reservation_passengers
VALUES
  (977, 3, 3, 1, 'Economy', 'Chips');

INSERT INTO
  legs
VALUES
  (977, 1, 'JB', 777, 2);

INSERT INTO
  persons
VALUES
  (
    6,
    'Lynard',
    'Skynard',
    'SomeWhere',
    'Else',
    'Home',
    12345,
    1234567899
  );

INSERT INTO
  app_users (username, password, person_id)
VALUES
  ('bross@email.com', 'password', 4),
  ('best@customerreps.com', 'password', 5),
  ('skynyrd03@freebird.com', 'password', 6);

INSERT INTO
  employees
VALUES
  (6, 123654132, true, '2014-05-08', 150.00);

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
    pg_get_serial_sequence('customers', 'account_number'),
    (
      SELECT
        MAX(account_number)
      FROM
        customers
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
    pg_get_serial_sequence('reservations', 'reservation_number'),
    (
      SELECT
        MAX(reservation_number)
      FROM
        reservations
    )
  );