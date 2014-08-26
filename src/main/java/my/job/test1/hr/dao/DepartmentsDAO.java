package my.job.test1.hr.dao;

import org.sql2o.Connection;

import java.sql.CallableStatement;
import java.sql.Types;

public class DepartmentsDAO {

    Connection connection;

    public DepartmentsDAO(Connection connection) {
        this.connection = connection;
    }

    public int insert(int locationId, String departmentName) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL DEPARTMENTS_INSERT(?, ?, ?) }")) {
            statement.setInt(1, locationId);
            statement.setString(2, departmentName);
            statement.registerOutParameter(3, Types.INTEGER);
            statement.executeUpdate();
            return statement.getInt(3);
        }
    }

    public void updateManager(int departmentId, int managerId) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL DEPARTMENTS_UPDATE_MANAGER(?, ?) }")) {
            statement.setInt(1, departmentId);
            statement.setInt(2, managerId);
            statement.executeUpdate();
        }
    }
}
