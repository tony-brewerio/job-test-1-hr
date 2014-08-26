--liquibase formatted sql

--changeset tonybr:2014-08-26--09 splitStatements:false

CREATE PROCEDURE DEPARTMENTS_UPDATE_MANAGER(in_department_id IN INTEGER,
                                            in_manager_id    IN INTEGER)
IS
  BEGIN
    UPDATE DEPARTMENTS
    SET manager_id = in_manager_id
    WHERE department_id = in_department_id;
  END DEPARTMENTS_UPDATE_MANAGER;

-- rollback DROP PROCEDURE DEPARTMENTS_UPDATE_MANAGER;
