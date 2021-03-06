--liquibase formatted sql

--changeset tonybr:2014-08-26--02

CREATE TABLE DEPARTMENTS (
  department_id   INTEGER      NOT NULL,
  department_name VARCHAR(100) NOT NULL,

  manager_id      INTEGER      NULL,
  location_id     INTEGER      NOT NULL,

  CONSTRAINT DEPARTMENTS_PK PRIMARY KEY (department_id),
  CONSTRAINT DEPARTMENTS_LOCATION_FK FOREIGN KEY (location_id) REFERENCES LOCATIONS ON DELETE CASCADE
);

CREATE SEQUENCE DEPARTMENTS_ID_SEQ;

CREATE INDEX DEPARTMENTS_MANAGER_FKI ON DEPARTMENTS (manager_id);
CREATE INDEX DEPARTMENTS_LOCATION_FKI ON DEPARTMENTS (location_id);

CREATE TABLE EMPLOYEES (
  employee_id   INTEGER       NOT NULL,
  first_name    VARCHAR(50)   NOT NULL,
  last_name     VARCHAR(50)   NOT NULL,
  email         VARCHAR(50)   NOT NULL,
  phone_number  VARCHAR(50)   NOT NULL,
  hire_date     TIMESTAMP     NULL,
  salary        DECIMAL(8, 2) NULL,
  comission_pct DECIMAL(8, 2) NULL,

  job_id        INTEGER       NULL,
  manager_id    INTEGER       NULL,
  department_id INTEGER       NULL,

  CONSTRAINT EMPLOYEES_PK PRIMARY KEY (employee_id),
  CONSTRAINT EMPLOYEES_MANAGER_FK FOREIGN KEY (manager_id) REFERENCES EMPLOYEES ON DELETE CASCADE,
  CONSTRAINT EMPLOYEES_DEPARTMENT_FK FOREIGN KEY (department_id) REFERENCES DEPARTMENTS ON DELETE CASCADE
);

CREATE SEQUENCE EMPLOYEES_ID_SEQ;

CREATE INDEX EMPLOYEES_JOB_FKI ON EMPLOYEES (job_id);
CREATE INDEX EMPLOYEES_MANAGER_FKI ON EMPLOYEES (manager_id);
CREATE INDEX EMPLOYEES_DEPARTMENT_FKI ON EMPLOYEES (department_id);

ALTER TABLE DEPARTMENTS
ADD CONSTRAINT DEPARTMENTS_MANAGER_FK
FOREIGN KEY (manager_id) REFERENCES EMPLOYEES ON DELETE CASCADE;

CREATE TABLE JOBS (
  job_id     INTEGER      NOT NULL,
  job_title  VARCHAR(100) NOT NULL,
  min_salary DECIMAL      NOT NULL,
  max_salary DECIMAL      NOT NULL,

  CONSTRAINT JOBS_PK PRIMARY KEY (job_id)
);

CREATE SEQUENCE JOBS_ID_SEQ;

ALTER TABLE EMPLOYEES
ADD CONSTRAINT EMPLOYEES_JOB_FK
FOREIGN KEY (job_id) REFERENCES JOBS ON DELETE CASCADE;

CREATE TABLE JOB_HISTORY (
  employee_id   INTEGER   NOT NULL,
  start_date    TIMESTAMP NOT NULL,
  end_date      TIMESTAMP NULL,

  job_id        INTEGER   NOT NULL,
  department_id INTEGER   NOT NULL,

  CONSTRAINT JOB_HISTORY_PK PRIMARY KEY (employee_id, start_date),
  CONSTRAINT JOB_HISTORY_EMPLOYEE_FK FOREIGN KEY (employee_id) REFERENCES EMPLOYEES ON DELETE CASCADE,
  CONSTRAINT JOB_HISTORY_JOB_FK FOREIGN KEY (job_id) REFERENCES JOBS ON DELETE CASCADE,
  CONSTRAINT JOB_HISTORY_DEPARTMENT_FK FOREIGN KEY (department_id) REFERENCES DEPARTMENTS ON DELETE CASCADE
);


-- rollback DROP SEQUENCE JOBS_ID_SEQ;
-- rollback DROP SEQUENCE EMPLOYEES_ID_SEQ;
-- rollback DROP SEQUENCE DEPARTMENTS_ID_SEQ;
-- rollback DROP TABLE JOB_HISTORY CASCADE CONSTRAINTS;
-- rollback DROP TABLE JOBS CASCADE CONSTRAINTS;
-- rollback DROP TABLE EMPLOYEES CASCADE CONSTRAINTS;
-- rollback DROP TABLE DEPARTMENTS CASCADE CONSTRAINTS;
