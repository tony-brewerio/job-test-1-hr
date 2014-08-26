package my.job.test1.hr.dao;

import org.sql2o.Connection;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.sql.Types;

public class EmployeesDAO {

    Connection connection;

    public EmployeesDAO(Connection connection) {
        this.connection = connection;
    }

    public int insert(String firstName, String lastName, String email, String phoneNumber) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL EMPLOYEES_INSERT(?, ?, ?, ?, ?) }")) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.registerOutParameter(5, Types.INTEGER);
            statement.executeUpdate();
            return statement.getInt(5);
        }
    }

    public void updateManager(int employeeId, int managerId) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL EMPLOYEES_UPDATE_MANAGER(?, ?) }")) {
            statement.setInt(1, employeeId);
            statement.setInt(2, managerId);
            statement.executeUpdate();
        }
    }

    public void jobStart(int departmentId, int employeeId, int jobId, Timestamp startDate, BigDecimal salary, BigDecimal comissionPct) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL EMPLOYEES_JOB_START(?, ?, ?, ?, ?, ?) }")) {
            statement.setInt(1, departmentId);
            statement.setInt(2, employeeId);
            statement.setInt(3, jobId);
            statement.setTimestamp(4, startDate);
            statement.setBigDecimal(5, salary);
            statement.setBigDecimal(6, comissionPct);
            statement.executeUpdate();
        }
    }

    public void jobEnd(int employeeId, Timestamp endDate) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL EMPLOYEES_JOB_END(?, ?) }")) {
            statement.setInt(1, employeeId);
            statement.setTimestamp(2, endDate);
            statement.executeUpdate();
        }
    }

}
