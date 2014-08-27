package my.job.test1.hr.application;

import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.io.IOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static my.job.test1.hr.Application.getConnection;

public class QueryDepartmentsStats implements ICommand {

    @Override
    public void run(Namespace ns) throws Exception {
        double years = ns.get("years");
        int staff = ns.get("staff");

        System.out.printf("Invoking query DEPARTMENT_STATS with { staff: %s , years: %s } \n\n", staff, years);

        String sql = IOUtils.toString(getClass().getResourceAsStream("/sql/QueryDepartmentsStats.sql"), "UTF-8");
        String rowFormat = "| %8s | %8s | %8s | %8s | %12s | %26s | %16s | %12s |\n";
        System.out.printf(
                rowFormat,
                "DEPT_ID", "EMP_CNT", "SLR_MIN", "SLR_MAX", "SUM_SLR_VET",
                "DEPT_NAME", "COUNTRY_NAME", "REGION_NAME"
        );
        System.out.println("|" + new String(new char[121]).replace("\0", "=") + "|");

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, years);
            statement.setInt(2, staff);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                System.out.printf(
                        rowFormat,
                        rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
                        rs.getString(6), rs.getString(7), rs.getString(8)
                );
            }
        }
    }

}
