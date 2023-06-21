package collectedgarbage.DBtoDBMigrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class DatabaseConfig {

    private final Environment env;

    @Primary
    @Bean(name = "source")
    public DataSource Source() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.source.dataSource.driverClassName")));
        dataSource.setUrl(env.getProperty("spring.source.dataSource.url"));
        dataSource.setUsername(env.getProperty("spring.source.dataSource.username"));
        dataSource.setPassword(env.getProperty("spring.source.dataSource.password"));

        log.info("Source Connection is Ready");

        return dataSource;
    }

    @Bean(name = "dest")
    public DataSource Dest() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.dest.dataSource.driverClassName")));
        dataSource.setUrl(env.getProperty("spring.dest.dataSource.url"));
        dataSource.setUsername(env.getProperty("spring.dest.dataSource.username"));
        dataSource.setPassword(env.getProperty("spring.dest.dataSource.password"));

        log.info("Destination Connection is Ready");

        return dataSource;
    }
}
