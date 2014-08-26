package my.job.test1.hr.dao;

import org.sql2o.Connection;

import java.sql.CallableStatement;
import java.sql.Types;

public class RegionsDAO {

    Connection connection;

    public RegionsDAO(Connection connection) {
        this.connection = connection;
    }

    public int insert(String regionName) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL REGIONS_INSERT(?, ?) }")) {
            statement.setString(1, regionName);
            statement.registerOutParameter(2, Types.INTEGER);
            statement.executeUpdate();
            return statement.getInt(2);
        }
    }

}
