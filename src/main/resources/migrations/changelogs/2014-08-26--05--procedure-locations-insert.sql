--liquibase formatted sql

--changeset tonybr:2014-08-26--05 splitStatements:false

CREATE PROCEDURE LOCATIONS_INSERT(in_country_id IN INTEGER, in_street_address IN STRING, in_postal_code IN STRING,
                                  in_city       IN STRING, in_state_province STRING, out_location_id OUT INTEGER)
IS
  BEGIN
    out_location_id := LOCATIONS_ID_SEQ.nextval;
    INSERT INTO LOCATIONS (location_id, street_address, postal_code, city, state_province, country_id)
    VALUES (out_location_id, in_street_address, in_postal_code, in_city, in_state_province, in_country_id);
  END LOCATIONS_INSERT;

-- rollback DROP PROCEDURE LOCATIONS_INSERT;
