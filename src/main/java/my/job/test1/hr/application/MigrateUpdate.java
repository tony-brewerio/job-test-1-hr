package my.job.test1.hr.application;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import my.job.test1.hr.Application;
import net.sourceforge.argparse4j.inf.Namespace;

import java.sql.Connection;

import static my.job.test1.hr.Application.getConnection;

public class MigrateUpdate implements ICommand {

    @Override
    public void run(Namespace ns) throws Exception {
        try (Connection connection = getConnection()) {
            JdbcConnection jdbcConnection = new JdbcConnection(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
            Liquibase liquibase = new Liquibase(Application.getConfig().liquibaseChangelog(), new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
        }
    }

}
