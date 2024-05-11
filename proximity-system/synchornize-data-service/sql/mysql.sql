CREATE SCHEMA IF NOT EXISTS synchronization;

CREATE TABLE IF NOT EXISTS synchronization.es_shard_location(
    es_id INT AUTO_INCREMENT  PRIMARY KEY,
    es_name varchar(255),
    es_ip_address varchar(50),
    es_host int,
    last_update_date TIMESTAMP NOT NULL
)