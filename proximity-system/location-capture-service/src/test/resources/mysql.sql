CREATE SCHEMA IF NOT EXISTS business;


CREATE TABLE IF NOT EXISTS business.business_information(
    business_id varchar(255) primary key ,
    name varchar(255),
    business_type ENUM('GAS_STATION','CONVENIENCE_STORE'),
    description varchar(255)
)