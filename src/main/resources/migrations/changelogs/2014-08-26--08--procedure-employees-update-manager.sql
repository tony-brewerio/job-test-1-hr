--liquibase formatted sql

--changeset tonybr:2014-08-26--08 splitStatements:false

CREATE PROCEDURE EMPLOYEES_UPDATE_MANAGER(in_employee_id IN INTEGER,
                                          in_manager_id  IN INTEGER)
IS
  BEGIN
    UPDATE EMPLOYEES SET manager_id = in_manager_id WHERE employee_id = in_employee_id;
  END EMPLOYEES_UPDATE_MANAGER;

-- rollback DROP PROCEDURE EMPLOYEES_UPDATE_MANAGER;
