package my.job.test1.hr.dao;

import org.sql2o.Connection;

import java.sql.CallableStatement;
import java.sql.Types;

public class CountriesDAO {

    Connection connection;

    public CountriesDAO(Connection connection) {
        this.connection = connection;
    }

    public int insert(int regionId, String countryName) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL COUNTRIES_INSERT(?, ?, ?) }")) {
            statement.setInt(1, regionId);
            statement.setString(2, countryName);
            statement.registerOutParameter(3, Types.INTEGER);
            statement.executeUpdate();
            return statement.getInt(3);
        }
    }

}
