package collectedgarbage.DBtoDBMigrator;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class DatabaseConfig {

    private final Environment env;

    @Primary
    @Bean(name = "source")
    public DataSource Source() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.source.dataSource.driverClassName"));
        dataSource.setJdbcUrl(env.getProperty("spring.source.dataSource.url"));
        dataSource.setUsername(env.getProperty("spring.source.dataSource.username"));
        dataSource.setPassword(env.getProperty("spring.source.dataSource.password"));

        log.info("Source Connection is Ready");

        return dataSource;
    }

    @Bean(name = "dest")
    public DataSource Dest() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.dest.dataSource.driverClassName"));
        dataSource.setJdbcUrl(env.getProperty("spring.dest.dataSource.url"));
        dataSource.setUsername(env.getProperty("spring.dest.dataSource.username"));
        dataSource.setPassword(env.getProperty("spring.dest.dataSource.password"));

        log.info("Destination Connection is Ready");

        return dataSource;
    }
}
