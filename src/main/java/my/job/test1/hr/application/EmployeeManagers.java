package my.job.test1.hr.application;

import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static my.job.test1.hr.Application.getConnection;

public class EmployeeManagers implements ICommand {

    @Override
    public void run(Namespace ns) throws Exception {
        List<Integer> employeesIds = ns.get("id");
        boolean reverse = ns.getBoolean("reverse");

        String sql = IOUtils.toString(getClass().getResourceAsStream("/sql/QueryEmployeeManagers.sql"), "UTF-8");

        try (Connection connection = getConnection()) {
            for (int id : employeesIds) {
                System.out.printf("Employee managers hierarchy for employee [%10s], reverse: [%5s] => ", id, reverse);
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        // SYS_CONNECT_BY_PATH will always add root / that is not needed, and must be stripped out
                        String[] hierarchy = StringUtils.strip(rs.getString(1), "/").split("/");
                        if (reverse) {
                            ArrayUtils.reverse(hierarchy);
                        }
                        System.out.print(StringUtils.join(hierarchy, " / "));
                    } else {
                        System.out.print("! no employee with this ID found in database !");
                    }
                }
                System.out.println();
            }
        }
    }

}
