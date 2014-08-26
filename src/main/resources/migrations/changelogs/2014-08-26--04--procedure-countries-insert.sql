--liquibase formatted sql

--changeset tonybr:2014-08-26--04 splitStatements:false

CREATE PROCEDURE COUNTRIES_INSERT(in_region_id IN INTEGER, in_country_name IN STRING, out_country_id OUT INTEGER)
IS
  BEGIN
    out_country_id := COUNTRIES_ID_SEQ.nextval;
    INSERT INTO COUNTRIES (country_id, country_name, region_id)
    VALUES (out_country_id, in_country_name, in_region_id);
  END COUNTRIES_INSERT;

-- rollback DROP PROCEDURE COUNTRIES_INSERT;
