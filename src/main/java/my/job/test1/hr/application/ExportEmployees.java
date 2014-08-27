package my.job.test1.hr.application;

import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static my.job.test1.hr.Application.getConnection;

public class ExportEmployees implements ICommand {

    @Override
    public void run(Namespace ns) throws Exception {
        File file = ns.get("file");
        boolean header = ns.getBoolean("header");

        System.out.printf("Proceeding to export EMPLOYEES data into TSV file named: %s\n", file.getAbsolutePath());
        if (header) {
            System.out.println("Header with column names will be added to TSV file");
        }
        System.out.println();

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM EMPLOYEES ORDER BY EMPLOYEE_ID");
                ResultSet rs = statement.executeQuery();
                if (header) {
                    List<String> columns = new ArrayList<>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        columns.add(rs.getMetaData().getColumnName(i));
                    }
                    writer.write(StringUtils.join(columns, "\t") + "\n");
                }
                while (rs.next()) {
                    List<String> columns = new ArrayList<>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        columns.add(rs.getString(i));
                    }
                    writer.write(StringUtils.join(columns, "\t") + "\n");
                }
            }
        }
    }

}
