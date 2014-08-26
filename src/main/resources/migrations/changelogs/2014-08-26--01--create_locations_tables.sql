--liquibase formatted sql

--changeset tonybr:2014-08-26--01

CREATE TABLE REGIONS (
  region_id   INTEGER PRIMARY KEY NOT NULL,
  region_name VARCHAR(50)         NOT NULL
);

CREATE TABLE COUNTRIES (
  country_id   INTEGER PRIMARY KEY NOT NULL,
  country_name VARCHAR(50)         NOT NULL,

  region_id REFERENCES REGIONS     NOT NULL
);

CREATE TABLE LOCATIONS (
  location_id    INTEGER PRIMARY KEY  NOT NULL,
  street_address VARCHAR(200)         NOT NULL,
  postal_code    VARCHAR(20)          NOT NULL,
  city           VARCHAR(50)          NOT NULL,
  state_province VARCHAR(50)          NOT NULL,

  country_id REFERENCES COUNTRIES     NOT NULL
);

-- rollback DROP TABLE COUNTRIES CASCADE CONSTRAINTS;
-- rollback DROP TABLE LOCATIONS CASCADE CONSTRAINTS;
-- rollback DROP TABLE REGIONS CASCADE CONSTRAINTS;
