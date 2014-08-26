--liquibase formatted sql

--changeset tonybr:2014-08-26--03 splitStatements:false

CREATE PROCEDURE REGIONS_INSERT(in_region_name IN STRING, out_region_id OUT INTEGER)
IS
  BEGIN
    out_region_id := REGIONS_ID_SEQ.nextval;
    INSERT INTO REGIONS (region_id, region_name)
    VALUES (out_region_id, in_region_name);
  END REGIONS_INSERT;

-- rollback DROP PROCEDURE REGIONS_INSERT;
