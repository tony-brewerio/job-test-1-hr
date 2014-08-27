package my.job.test1.hr.application;

import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static my.job.test1.hr.Application.getConnection;

public class EmployeeSalary implements ICommand {

    final static int FETCH_CURRENCY_TIMEOUT = 1000 * 10;  // 10 seconds

    @Override
    public void run(Namespace ns) throws Exception {
        List<Integer> employeesIds = ns.get("id");

        BigDecimal exchangeRate = fetchLocalCurrencyExchangeRate();
        if (exchangeRate == null) {
            System.out.println("Bank API did not return USD currency");
        } else {
            System.out.println("Bank API returns today's KZT <> USD echange rate => " + exchangeRate);
        }

        try (Connection connection = getConnection()) {
            for (int id : employeesIds) {
                System.out.printf("Employee salary for [%10s] => ", id);
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT SALARY FROM EMPLOYEES WHERE EMPLOYEE_ID = ?"
                );
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        BigDecimal salary = rs.getBigDecimal(1);
                        if (rs.getObject(1) == null) {
                            System.out.print("! employee is currently not employed, i.e. has no salary set !");
                        } else {
                            System.out.printf("%s", DecimalFormat.getCurrencyInstance(Locale.US).format(salary));
                            if (exchangeRate != null) {
                                System.out.printf(" / %s", new DecimalFormat("#,###.00 KZT").format(salary.multiply(exchangeRate)));
                            }
                        }
                    } else {
                        System.out.print("! no employee with this ID found in database !");
                    }
                }
                System.out.println();
            }
        }
    }

    public BigDecimal fetchLocalCurrencyExchangeRate() throws Exception {
        Date today = new Date();
        URL url = new URIBuilder("http://www.nationalbank.kz/rss/get_rates.cfm")
                .addParameter("fdate", new SimpleDateFormat("dd.MM.yyyy").format(today))
                .build()
                .toURL();
        Document document = Jsoup.parse(url, FETCH_CURRENCY_TIMEOUT);
        Map<String, BigDecimal> rates = new HashMap<>();
        for (Element item : document.getElementsByTag("item")) {
            String title = item.getElementsByTag("title").first().text();
            String description = item.getElementsByTag("description").first().text();
            rates.put(title, new BigDecimal(description));
        }
        return rates.get("USD");
    }

}
