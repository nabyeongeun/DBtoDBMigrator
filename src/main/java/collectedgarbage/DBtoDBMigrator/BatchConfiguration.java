package collectedgarbage.DBtoDBMigrator;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    @Resource(name = "source")
    private final DataSource source;

    @Resource(name = "dest")
    private final DataSource dest;

    public static int pageSize = 10000;

    @Bean
    public PagingQueryProvider provider() throws Exception {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(source);
        provider.setSelectClause("sample_key, sample_data");
        provider.setFromClause("from sample");
//        provider.setWhereClause("where first_name >= :fisrt_name"); // match variable
        provider.setSortKeys(Map.of("sample_key", Order.ASCENDING));

        return provider.getObject();
    }

    @Bean
    public JdbcPagingItemReader<Sample> reader() throws Exception {

        return new JdbcPagingItemReaderBuilder<Sample>()
                .dataSource(source)
                .pageSize(pageSize)
                .queryProvider(provider())
//                .parameterValues(Map.of("first_name", "Alika")) // match variable
                .rowMapper(new BeanPropertyRowMapper<>(Sample.class))
                .name("jdbcPagingItemReader")
                .build();
    }

    @Bean
    public ItemProcessor processor() {
        return new ItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Sample> writer() {
        return new JdbcBatchItemWriterBuilder<Sample>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO sample (sample_key, sample_data) VALUES (:sampleKey, :sampleData)")
                .dataSource(dest)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Sample> writer) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<Sample, Sample> chunk(pageSize, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}