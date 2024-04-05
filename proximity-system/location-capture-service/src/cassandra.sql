CREATE KEYSPACE IF NOT EXISTS location WITH REPLICATION = {'class':
    'SimpleStrategy', 'replication_factor' : 3};

CREATE TABLE location.location_capture(
                                          business_id text primary key ,
                                          lat double,
                                          long double,
                                          timestamp timestamp
)