--liquibase formatted sql

--changeset tonybr:2014-08-26--10 splitStatements:false

CREATE PROCEDURE JOBS_INSERT(in_job_title  IN  STRING,
                             in_min_salary IN  DECIMAL,
                             in_max_salary IN  DECIMAL,
                             out_job_id    OUT INTEGER)
IS
  BEGIN
    out_job_id := JOBS_ID_SEQ.nextval;
    INSERT INTO JOBS (job_id, job_title, min_salary, max_salary)
    VALUES (out_job_id, in_job_title, in_min_salary, in_max_salary);
  END JOBS_INSERT;

-- rollback DROP PROCEDURE JOBS_INSERT;
