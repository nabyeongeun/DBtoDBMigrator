package collectedgarbage.DBtoDBMigrator;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    @Resource(name = "source")
    private final DataSource source;

    @Resource(name = "dest")
    private final DataSource dest;

    public static int pageSize = 10;

    @Bean
    public PagingQueryProvider provider() throws Exception {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(source);
        provider.setSelectClause("first_name, last_name");
        provider.setFromClause("from people");
//        provider.setWhereClause("where first_name >= :fisrt_name"); // match variable
        provider.setSortKeys(Map.of("first_name", Order.ASCENDING));

        return provider.getObject();
    }

    @Bean
    public JdbcPagingItemReader<Person> reader() throws Exception {

        return new JdbcPagingItemReaderBuilder<Person>()
                .dataSource(source)
                .pageSize(pageSize)
                .queryProvider(provider())
//                .parameterValues(Map.of("first_name", "Alika")) // match variable
                .rowMapper(new BeanPropertyRowMapper<>(Person.class))
//                .maxItemCount()
//                .maxRows()
                .name("jdbcPagingItemReader")
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer() {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dest)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter<Person> writer) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person> chunk(pageSize, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}