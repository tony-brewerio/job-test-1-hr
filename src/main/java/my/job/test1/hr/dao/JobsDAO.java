package my.job.test1.hr.dao;

import org.sql2o.Connection;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;

public class JobsDAO {

    Connection connection;

    public JobsDAO(Connection connection) {
        this.connection = connection;
    }

    public int insert(String jobTitle, BigDecimal minSalary, BigDecimal maxSalary) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL JOBS_INSERT(?, ?, ?, ?) }")) {
            statement.setString(1, jobTitle);
            statement.setBigDecimal(2, minSalary);
            statement.setBigDecimal(3, maxSalary);
            statement.registerOutParameter(4, Types.INTEGER);
            statement.executeUpdate();
            return statement.getInt(4);
        }
    }

}
