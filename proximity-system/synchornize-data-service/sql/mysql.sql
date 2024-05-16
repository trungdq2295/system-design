CREATE SCHEMA IF NOT EXISTS synchronization;

CREATE TABLE IF NOT EXISTS synchronization.es_shard_location(
                                                                es_id INT AUTO_INCREMENT  PRIMARY KEY,
                                                                es_name varchar(255),
    es_ip_address varchar(50),
    es_port int,
    last_update_date TIMESTAMP NOT NULL
    );

insert into synchronization.es_shard_location
    (es_name, es_ip_address, es_port, last_update_date)
VALUES
("es01","localhost",9200,SUBDATE(NOW(),1)),
("es02","localhost",9300,SUBDATE(NOW(),1)),
("es03","localhost",9400,SUBDATE(NOW(),1));