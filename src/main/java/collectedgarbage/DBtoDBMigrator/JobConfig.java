package collectedgarbage.DBtoDBMigrator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
//@EnableBatchProcessing(dataSourceRef = "testDataSource", transactionManagerRef = "testTransactionManager")
public class JobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job testSimpleJob(JobRepository jobRepository, Step testStep){
        return new JobBuilder("testSimpleJob", jobRepository)
                .start(testStep)
                .build();

    }

    @Bean
    public Step testStep(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("testStep", jobRepository)
                .tasklet(testTasklet, platformTransactionManager).build();
    }

    @Bean
    public Tasklet testTasklet(){
        return ((contribution, chunkContext) -> {
            System.out.println("테스트1");

            return RepeatStatus.FINISHED;
        });
    }


}
