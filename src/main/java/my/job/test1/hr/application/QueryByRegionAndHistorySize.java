package my.job.test1.hr.application;

import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.io.IOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static my.job.test1.hr.Application.getConnection;

public class QueryByRegionAndHistorySize implements ICommand {

    @Override
    public void run(Namespace ns) throws Exception {
        List<String> regions = ns.get("regions");
        int jobs = ns.get("jobs");

        System.out.printf(
                "Invoking query BY_REGIONS_AND_JOBS with { regions: %s , jobs: %s } \n\n",
                regions, jobs
        );

        String sql = IOUtils.toString(getClass().getResourceAsStream("/sql/QueryByRegionAndHistorySize.sql"), "UTF-8");
        String inClause = "?";
        for (int i = 1; i < regions.size(); i++) {
            inClause += ",?";
        }
        sql = sql.replace("?regions?", inClause);

        String rowFormat = "| %10s | %24s | %20s | %12s |\n";
        System.out.printf(rowFormat, "ID", "FULL NAME", "JOB TITLE", "TRANSITIONS");
        System.out.println("|" + new String(new char[73]).replace("\0", "=") + "|");

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < regions.size(); i++) {
                statement.setString(i + 1, regions.get(i));
            }
            statement.setInt(regions.size() + 1, jobs);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                System.out.printf(rowFormat, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
            }
        }
    }

}
