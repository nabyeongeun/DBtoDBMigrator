package collectedgarbage.DBtoDBMigrator.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class SampleListener implements JobExecutionListener {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

//            jdbcTemplate.query("SELECT sample_key, sample_data FROM sample",
//                    (rs, row) -> new Sample(
//                            rs.getLong(1),
//                            rs.getString(2))
//            ).forEach(sample -> log.info("Found <{{}}> in the database.", sample));
        }
    }
}
