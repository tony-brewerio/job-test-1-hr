Sample Java / Oracle command line app
=========

This is a simple Java command line application I've done for job interview.
With the purpose of getting some useful experience from writing it, I over-engineered quite a bit.

Application entry point is `Application` class that uses neat argparse4j library to handle command line arguments.
Functionality is split into separate commands, which are simple classes that implement `ICommand` functional interface.
argparse4j allows to add ghost parameters with default values, inaccessible to the client from command line,
commands classes are instantiated and registered as such parameters.
Each command and parameter have help strings that describe their purpose to the user.

First two commands, `migrate-update` and `migrate-rollback` use Liquibase Java API to either apply or rollback migrations.
Migrations scripts use raw SQL format and are located in `resources/migrations/`.

Next command is `generate-data`, it fills the database with randomly generated records. Couple of DAO classes are used exclusively by this command, and forward calls directly to Oracle stored procedures. Java code itself contains no *INSERT*  or *UPDATE* statements. Generator assumes that database was updated to newest version already.

Query command `query-rh` shows list of employees that work in one of the *regions* and have at least *n* of job transfers. Both  *regions* and *n* can be provided by the user, `["AMERICA", "EUROPE"]` and `2` are defaults.
```
Invoking query BY_REGIONS_AND_JOBS with { regions: [AMERICA, EUROPE] , jobs: 4 } 

|         ID |                FULL NAME |            JOB TITLE |  TRANSITIONS |
|=============================================================================|
|      16647 |            Travis Blanda |     Portfurt MissSr. |            5 |
|      16669 |           Kayley Goodwin |     Portfurt MissSr. |            4 |
|      16683 |          Isabelle Littel |        Newfurt Dr.MD |            4 |
```

Query command `query-ds` displays some statistics for departments. Shows only departments that have at least *n* employees . Also calculates total monthly salary spent on workers that have at least *years* of experience. Defaults are `2` staff members and `3` years.
```
Invoking query DEPARTMENT_STATS with { staff: 3 , years: 3.0 } 

|  DEPT_ID |  EMP_CNT |  SLR_MIN |  SLR_MAX |  SUM_SLR_VET |                  DEPT_NAME |     COUNTRY_NAME |  REGION_NAME |
|=========================================================================================================================|
|     2081 |        4 |     1382 |     2302 |         4375 |      Westborough Northstad |          America |      AMERICA |
|     2083 |        5 |     1467 |     2650 |         7494 |          Newland Northview |          America |      AMERICA |
|     2085 |        5 |     1686 |     2010 |         5558 |        Southshire Lakestad |          America |      AMERICA |
|     2087 |        6 |     1002 |     2687 |         9533 |       Portshire Northville |          America |      AMERICA |
|     2089 |        6 |     1076 |     2565 |         2454 |        Northberg Westmouth |          America |      AMERICA |
```

Both query commands use ascii table-like style to display data.

Command `export-employees` loads entire employees table and saves it into TSV file. File name must me provided by user, and optionally, header with column names may be added to the file.

Command `employee-managers` displays management hierarchy for given employees, by ID. Accepts list of ID's, space-separated. By default, display order is bottom -> top, but if *reverse* flag is set, it will be top -> bottom. Uses Oracle's `CONNECT BY` functionality.
```
Employee managers hierarchy for employee [     16922], reverse: [ true] => Bridie Collier / Meaghan Hansen / Johnathan Hermiston
Employee managers hierarchy for employee [     16931], reverse: [ true] => Keven Stamm / Alverta Denesik / Gaylord McGlynn
Employee managers hierarchy for employee [    100500], reverse: [ true] => ! no employee with this ID found in database !
```

Command `employee-salary` displays employees salary both in USD and KZT ( my local currency ). 
Employees are also searched by ID, and list of ID's can be specified.
Current exchange rate is pulled from XML feed of the local country bank.
```
Bank API returns today's KZT <> USD echange rate => 182.00
Employee salary for [     16922] => $2,040.00 / 371,280.00 KZT
Employee salary for [     16931] => ! employee is currently not employed, i.e. has no salary set !
Employee salary for [    100500] => ! no employee with this ID found in database !
```
