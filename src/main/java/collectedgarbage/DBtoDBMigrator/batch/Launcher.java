package collectedgarbage.DBtoDBMigrator.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Launcher {

    private final JobLauncher jobLauncher;
    private final Job sampleJob;
    private final Job sample2Job;

    @Bean
    @ConditionalOnExpression("'${jobProperty}' == 'sample' or '${jobProperty}' == 'ALL'")
    public void sampleRunner() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        JobParameters jobParameters = jobParametersBuilder.addString("id", "chong").toJobParameters();
        jobLauncher.run(sampleJob,jobParameters);
    }

    @Bean
    @ConditionalOnExpression("'${jobProperty}' == 'sample2' or '${jobProperty}' == 'ALL'")
    public void sample2Runner() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        JobParameters jobParameters = jobParametersBuilder.addString("id", "chong").toJobParameters();
        jobLauncher.run(sample2Job,jobParameters);
    }
}
