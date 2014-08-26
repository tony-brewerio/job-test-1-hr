package my.job.test1.hr.dao;

import org.sql2o.Connection;

import java.sql.CallableStatement;
import java.sql.Types;

public class LocationsDAO {

    Connection connection;

    public LocationsDAO(Connection connection) {
        this.connection = connection;
    }

    public int insert(int countryId, String streetAddress, String postalCode, String city, String stateProvince) throws Exception {
        try (CallableStatement statement = connection.getJdbcConnection().prepareCall("{ CALL LOCATIONS_INSERT(?, ?, ?, ?, ?, ?) }")) {
            statement.setInt(1, countryId);
            statement.setString(2, streetAddress);
            statement.setString(3, postalCode);
            statement.setString(4, city);
            statement.setString(5, stateProvince);
            statement.registerOutParameter(6, Types.INTEGER);
            statement.executeUpdate();
            return statement.getInt(6);
        }
    }

}
