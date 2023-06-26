package collectedgarbage.DBtoDBMigrator.batch.sample2;

import jakarta.annotation.Resource;
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
public class Sample2Config {

    @Resource(name = "source")
    private DataSource source;

    @Resource(name = "dest")
    private DataSource dest;

    public static int pageSize = 10000;

    @Bean
    public PagingQueryProvider provider2() throws Exception {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(source);
        provider.setSelectClause("sample_key, sample_data");
        provider.setFromClause("from sample2");
//        provider.setWhereClause("where first_name >= :fisrt_name"); // match variable
        provider.setSortKeys(Map.of("sample_key", Order.ASCENDING));

        return provider.getObject();
    }

    @Bean
    public JdbcPagingItemReader<Sample2> reader2() throws Exception {

        return new JdbcPagingItemReaderBuilder<Sample2>()
                .dataSource(source)
                .pageSize(pageSize)
                .queryProvider(provider2())
//                .parameterValues(Map.of("first_name", "Alika")) // match variable
                .rowMapper(new BeanPropertyRowMapper<>(Sample2.class))
                .name("jdbcPagingItemReader")
                .build();
    }

    @Bean
    public Sample2Processor processor2() {
        return new Sample2Processor();
    }

    @Bean
    public JdbcBatchItemWriter<Sample2> writer2() {
        return new JdbcBatchItemWriterBuilder<Sample2>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO sample2 (sample_key, sample_data) VALUES (:sampleKey, :sampleData)")
                .dataSource(dest)
                .build();
    }

    @Bean
    public Job sample2(JobRepository jobRepository, Sample2Listener listener, Step step1) {
        return new JobBuilder("sample2", jobRepository)
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Sample2> writer) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<Sample2, Sample2> chunk(pageSize, transactionManager)
                .reader(reader2())
                .processor(processor2())
                .writer(writer)
                .build();
    }

}