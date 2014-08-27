package my.job.test1.hr.application;

import com.github.javafaker.Faker;
import my.job.test1.hr.Application;
import my.job.test1.hr.dao.*;
import net.sourceforge.argparse4j.inf.Namespace;
import org.sql2o.Connection;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

public class GenerateData implements ICommand {

    Connection connection;
    RegionsDAO regionsDAO;
    CountriesDAO countriesDAO;
    LocationsDAO locationsDAO;
    DepartmentsDAO departmentsDAO;
    EmployeesDAO employeesDAO;
    JobsDAO jobsDAO;

    Random random = new Random();

    Faker faker = new Faker();

    long hireDateFrom = Timestamp.valueOf("2010-01-01 00:00:00").getTime();
    long hireDateTo = Timestamp.valueOf("2011-01-01 00:00:00").getTime();

    String[][] regionsWithCountries = {
            {"AMERICA", "America", "Canada"},
            {"ASIA", "Kazakhstan", "China", "Japan", "India"},
            {"EUROPE", "Russia", "France", "United Kingdom", "Italy"}
    };

    int[] jobsIds = new int[10];

    @Override
    public void run(Namespace ns) throws Exception {
        try (Connection connection = Application.getSql2o().beginTransaction()) {
            this.connection = connection;

            regionsDAO = new RegionsDAO(connection);
            countriesDAO = new CountriesDAO(connection);
            locationsDAO = new LocationsDAO(connection);
            departmentsDAO = new DepartmentsDAO(connection);
            employeesDAO = new EmployeesDAO(connection);
            jobsDAO = new JobsDAO(connection);

            deleteAll();
            createJobs();
            createLocations();
            connection.commit();
        }
    }

    void deleteAll() throws Exception {
        connection.createQuery("DELETE FROM JOB_HISTORY").executeUpdate();
        connection.createQuery("DELETE FROM JOBS").executeUpdate();
        connection.createQuery("DELETE FROM EMPLOYEES").executeUpdate();
        connection.createQuery("DELETE FROM DEPARTMENTS").executeUpdate();
        connection.createQuery("DELETE FROM LOCATIONS").executeUpdate();
        connection.createQuery("DELETE FROM COUNTRIES").executeUpdate();
        connection.createQuery("DELETE FROM REGIONS").executeUpdate();
    }

    void createJobs() throws Exception {
        for (int i = 0; i < 10; i++) {
            jobsIds[i] = jobsDAO.insert(
                    faker.address().cityPrefix() + faker.address().citySuffix() + " " + faker.name().prefix() + faker.name().suffix(),
                    new BigDecimal(100 * i + 1000),
                    new BigDecimal(100 * i + 2000)
            );
        }
    }

    void createLocations() throws Exception {
        for (String[] regionWithCountries : regionsWithCountries) {
            int regionId = regionsDAO.insert(regionWithCountries[0]);
            for (int i = 1; i < regionWithCountries.length; i++) {
                int countryId = countriesDAO.insert(regionId, regionWithCountries[i]);
                for (int j = 0; j < 4; j++) {
                    int locationId = locationsDAO.insert(
                            countryId,
                            faker.address().streetAddress(false),
                            faker.address().zipCode(),
                            faker.address().cityPrefix() + faker.address().citySuffix(),
                            faker.address().stateAbbr()
                    );
                    createDepartments(locationId);
                }
            }
        }
    }

    void createDepartments(int locationId) throws Exception {
        for (int i = 0; i < 4; i++) {
            int departmentId = departmentsDAO.insert(
                    locationId,
                    faker.address().cityPrefix() + faker.address().citySuffix() + " "
                            + faker.address().cityPrefix() + faker.address().citySuffix()
            );
            createEmployees(departmentId);
        }
    }

    void createEmployees(int departmentId) throws Exception {
        createEmployees(departmentId, 0, 1);
    }

    void createEmployees(int departmentId, int managerId, int depth) throws Exception {
        int employeeId = employeesDAO.insert(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                faker.phoneNumber().phoneNumber()
        );
        if (managerId > 0) {
            employeesDAO.updateManager(employeeId, managerId);
        } else {
            departmentsDAO.updateManager(departmentId, employeeId);
        }
        createEmploymentHistory(departmentId, employeeId);
        if (departmentId % 2 == 0) {
            return;
        }
        if (depth <= 3) {
            createEmployees(departmentId, employeeId, depth + 1);
            createEmployees(departmentId, employeeId, depth + 1);
        }
    }

    void createEmploymentHistory(int departmentId, int employeeId) throws Exception {
        Timestamp now = new Timestamp(new Date().getTime());
        int jobsCount = random.nextInt(5) + 1;
        long datesDelta = (hireDateTo - hireDateFrom);
        Timestamp startDate = new Timestamp(hireDateFrom + randomJobDuration() / 2);
        Timestamp endDate;
        for (int i = 0; i < jobsCount; i++) {
            endDate = new Timestamp(startDate.getTime() + randomJobDuration() / 2 + datesDelta / 2);
            // because jobs have random duration, they can possibly go over current date
            // generator stops, if next generated job ends after now
            if (endDate.getTime() >= now.getTime()) {
                break;
            }
            int jobI = random.nextInt(10);
            int jobId = jobsIds[jobI];
            employeesDAO.jobStart(
                    departmentId, employeeId, jobId, startDate,
                    new BigDecimal(100 * jobI + 1000 + random.nextInt(1000)),
                    new BigDecimal(10 + 10 * Math.random())
            );
            employeesDAO.jobEnd(employeeId, endDate);
            startDate = new Timestamp(endDate.getTime() + randomJobDuration());
        }
        // add current, never ended, job to half of the employees
        // start date may be after now, no current job is assigned in that case
        if (random.nextBoolean() && startDate.getTime() < now.getTime()) {
            int jobI = random.nextInt(10);
            int jobId = jobsIds[jobI];
            employeesDAO.jobStart(
                    departmentId, employeeId, jobId, startDate,
                    new BigDecimal(100 * jobI + 1000 + random.nextInt(1000)),
                    new BigDecimal(10 + 10 * Math.random())
            );
        }
    }

    long randomJobDuration() {
        return (long) ((hireDateTo - hireDateFrom) * Math.random());
    }

}
