--liquibase formatted sql

--changeset tonybr:2014-08-26--07 splitStatements:false

CREATE PROCEDURE EMPLOYEES_INSERT(in_first_name   IN  STRING,
                                  in_last_name    IN  STRING,
                                  in_email        IN  STRING,
                                  in_phone_number IN  STRING,
                                  out_employee_id OUT INTEGER)
IS
  BEGIN
    out_employee_id := EMPLOYEES_ID_SEQ.nextval;
    INSERT INTO EMPLOYEES (employee_id, first_name, last_name, email,
                           phone_number)
    VALUES (out_employee_id, in_first_name, in_last_name, in_email,
            in_phone_number);
  END EMPLOYEES_INSERT;

-- rollback DROP PROCEDURE EMPLOYEES_INSERT;
