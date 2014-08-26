--liquibase formatted sql

--changeset tonybr:2014-08-26--06 splitStatements:false

CREATE PROCEDURE DEPARTMENTS_INSERT(in_location_id     IN  INTEGER,
                                    in_department_name IN  STRING,
                                    out_department_id  OUT INTEGER)
IS
  BEGIN
    out_department_id := DEPARTMENTS_ID_SEQ.nextval;
    INSERT INTO DEPARTMENTS (department_id, department_name, location_id)
    VALUES (out_department_id, in_department_name, in_location_id);
  END DEPARTMENTS_INSERT;

-- rollback DROP PROCEDURE DEPARTMENTS_INSERT;
