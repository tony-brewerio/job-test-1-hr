package my.job.test1.hr;

import my.job.test1.hr.application.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.*;
import org.aeonbits.owner.ConfigCache;
import org.sql2o.Sql2o;

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

    public static Sql2o getSql2o() {
        return new Sql2o(getConfig().jdbcUrl(), getConfig().jdbcUsername(), getConfig().jdbcPassword());
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

        Subparser parserGenerateData = subparsers
                .addParser("generate-data")
                .help("Fills database with generated random data, assumes all migrations are applied")
                .setDefault("command", new GenerateData());

        Subparser parserQueryByRegionAndHistorySize = subparsers
                .addParser("query-rh")
                .help("Queries database for employees from specific regions and who have certain number of job transitions")
                .setDefault("command", new QueryByRegionAndHistorySize());
        parserQueryByRegionAndHistorySize
                .addArgument("--regions")
                .help("Employees must work in one of the specified regions")
                .type(String.class)
                .nargs("+")
                .setDefault(new String[]{"AMERICA", "EUROPE"});
        parserQueryByRegionAndHistorySize
                .addArgument("--jobs")
                .help("Employees must have at least this much job transfers")
                .type(Integer.class)
                .setDefault(2);

        Subparser parserQueryDepartmentsStats = subparsers
                .addParser("query-ds")
                .help("Queries database for some departments stats, filters departments by staff size")
                .setDefault("command", new QueryDepartmentsStats());
        parserQueryDepartmentsStats
                .addArgument("--staff")
                .help("Will only show departments that have at least this much staff")
                .type(Integer.class)
                .setDefault(3);
        parserQueryDepartmentsStats
                .addArgument("--years")
                .help("Specifies how many years employee must work to be considered veteran")
                .type(Double.class)
                .setDefault(3d);

        Subparser parserExportEmployees = subparsers
                .addParser("export-employees")
                .help("Exports entire list of employees as TSV into specified file")
                .setDefault("command", new ExportEmployees());
        parserExportEmployees
                .addArgument("--header").action(Arguments.storeTrue());
        parserExportEmployees
                .addArgument("--file")
                .help("File that TSV data will be written to.")
                .type(Arguments.fileType().verifyCanCreate())
                .required(true);

        Subparser parserEmployeeManagers = subparsers
                .addParser("employee-managers")
                .help("Displays management hierarchy for given employees")
                .setDefault("command", new EmployeeManagers());
        parserEmployeeManagers
                .addArgument("id")
                .help("IDs of the employees")
                .nargs("+")
                .type(Integer.class)
                .required(true);
        parserEmployeeManagers
                .addArgument("--reverse")
                .help("If reverse is set, display managers top to bottom")
                .action(Arguments.storeTrue());

        Subparser parserEmployeeSalary = subparsers
                .addParser("employee-salary")
                .help("Displays salary for given employees, both in USD and local currency ( KZT )")
                .setDefault("command", new EmployeeSalary());
        parserEmployeeSalary
                .addArgument("id")
                .help("IDs of the employees")
                .nargs("+")
                .type(Integer.class)
                .required(true);

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
