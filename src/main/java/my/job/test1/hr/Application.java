package my.job.test1.hr;

import my.job.test1.hr.application.ICommand;
import my.job.test1.hr.application.MigrateRollback;
import my.job.test1.hr.application.MigrateUpdate;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;
import org.aeonbits.owner.ConfigCache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class Application {

    public static ApplicationConfig getConfig() {
        return ConfigCache.getOrCreate(ApplicationConfig.class);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getConfig().jdbcUrl(), getConfig().jdbcUsername(), getConfig().jdbcPassword());
    }

    public static void main(String[] args) throws Exception {
        // Connection to Oracle database with ru_RU locale fails with ORA-12705, and I use ru_RU Windows as a dev machine
        Locale.setDefault(Locale.ENGLISH);

        ArgumentParser parser = ArgumentParsers.newArgumentParser("hr");

        Subparsers subparsers = parser
                .addSubparsers()
                .title("commands")
                .metavar(" ");

        Subparser parserMigrateUpdate = subparsers
                .addParser("migrate-update")
                .help("Update database structure to the most recent version, using Liquibase")
                .setDefault("command", new MigrateUpdate());

        Subparser parserMigrateRollback = subparsers
                .addParser("migrate-rollback")
                .help("Rollback all applied migrations, using Liquibase")
                .setDefault("command", new MigrateRollback());

        try {
            Namespace ns = parser.parseArgs(args);
            ICommand command = ns.get("command");
            command.run(ns);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }

}
