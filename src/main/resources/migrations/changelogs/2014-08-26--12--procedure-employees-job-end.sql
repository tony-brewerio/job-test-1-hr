--liquibase formatted sql

--changeset tonybr:2014-08-26--12 splitStatements:false

CREATE PROCEDURE EMPLOYEES_JOB_END(in_employee_id IN INTEGER, in_end_date IN TIMESTAMP)
IS
  BEGIN
    UPDATE EMPLOYEES
    SET job_id      = NULL,
      salary        = NULL,
      comission_pct = NULL,
      department_id = NULL
    WHERE employee_id = in_employee_id;
    UPDATE JOB_HISTORY
    SET end_date = in_end_date
    WHERE employee_id = in_employee_id AND end_date IS NULL;
  END EMPLOYEES_JOB_END;

-- rollback DROP PROCEDURE EMPLOYEES_JOB_END;
