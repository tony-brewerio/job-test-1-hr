--liquibase formatted sql

--changeset tonybr:2014-08-26--01

CREATE TABLE REGIONS (
  region_id   INTEGER PRIMARY KEY NOT NULL,
  region_name VARCHAR(50)         NOT NULL
);

CREATE SEQUENCE REGIONS_ID_SEQ;

CREATE TABLE COUNTRIES (
  country_id   INTEGER PRIMARY KEY               NOT NULL,
  country_name VARCHAR(50)                       NOT NULL,

  region_id REFERENCES REGIONS ON DELETE CASCADE NOT NULL
);

CREATE SEQUENCE COUNTRIES_ID_SEQ;

CREATE INDEX COUNTRIES_REGION_FKI ON COUNTRIES(region_id);

CREATE TABLE LOCATIONS (
  location_id    INTEGER PRIMARY KEY                  NOT NULL,
  street_address VARCHAR(200)                         NOT NULL,
  postal_code    VARCHAR(20)                          NOT NULL,
  city           VARCHAR(50)                          NOT NULL,
  state_province VARCHAR(50)                          NOT NULL,

  country_id REFERENCES COUNTRIES ON DELETE CASCADE   NOT NULL
);

CREATE SEQUENCE LOCATIONS_ID_SEQ;

CREATE INDEX LOCATIONS_COUNTRY_FKI ON LOCATIONS(country_id);

-- rollback DROP SEQUENCE LOCATIONS_ID_SEQ;
-- rollback DROP SEQUENCE COUNTRIES_ID_SEQ;
-- rollback DROP SEQUENCE REGIONS_ID_SEQ;
-- rollback DROP TABLE COUNTRIES CASCADE CONSTRAINTS;
-- rollback DROP TABLE LOCATIONS CASCADE CONSTRAINTS;
-- rollback DROP TABLE REGIONS CASCADE CONSTRAINTS;
