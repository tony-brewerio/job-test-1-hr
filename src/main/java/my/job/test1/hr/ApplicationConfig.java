package my.job.test1.hr;

import org.aeonbits.owner.Config;

@Config.Sources({"file:ApplicationConfig.properties", "classpath:ApplicationConfig.properties"})
public interface ApplicationConfig extends Config {

    String jdbcUrl();

    String jdbcUsername();

    String jdbcPassword();

}
