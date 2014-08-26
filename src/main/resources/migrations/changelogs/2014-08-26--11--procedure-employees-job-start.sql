--liquibase formatted sql

--changeset tonybr:2014-08-26--11 splitStatements:false

CREATE PROCEDURE EMPLOYEES_JOB_START(in_department_id IN INTEGER,
                                     in_employee_id   IN INTEGER,
                                     in_job_id        IN INTEGER,
                                     in_start_date    IN TIMESTAMP,
                                     in_salary        IN DECIMAL,
                                     in_comission_pct IN DECIMAL)
IS
  BEGIN
    UPDATE EMPLOYEES
    SET department_id = in_department_id,
      job_id          = in_job_id,
      hire_date       = coalesce(hire_date, in_start_date),
      salary          = in_salary,
      comission_pct   = in_comission_pct
    WHERE employee_id = in_employee_id;
    INSERT INTO JOB_HISTORY (employee_id, start_date, job_id, department_id)
    VALUES (in_employee_id, in_start_date, in_job_id, in_department_id);
  END EMPLOYEES_JOB_START;

-- rollback DROP PROCEDURE EMPLOYEES_JOB_START;
