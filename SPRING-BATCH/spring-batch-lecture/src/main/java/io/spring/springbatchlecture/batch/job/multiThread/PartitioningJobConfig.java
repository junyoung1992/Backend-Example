package io.spring.springbatchlecture.batch.job.multiThread;

import io.spring.springbatchlecture.batch.listener.StopWatchJobListener;
import io.spring.springbatchlecture.batch.partitioner.ColumnRangePartitioner;
import io.spring.springbatchlecture.entity.Customer;
import io.spring.springbatchlecture.mapper.CustomerRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "partitioningJob")
public class PartitioningJobConfig extends DefaultBatchConfiguration {

    private final DataSource dataSource;
    private final int gridSize = 4;
    private final int chunkSize = 100;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("partitioningJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(masterStep(jobRepository, transactionManager))
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step masterStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner(slaveStep(jobRepository, transactionManager).getName(), partitioner())
                .step(slaveStep(jobRepository, transactionManager))
                .gridSize(gridSize)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step slaveStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("slaveStep", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(pagingItemReader(null, null))
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public Partitioner partitioner() {
        ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();

        columnRangePartitioner.setColumn("id");
        columnRangePartitioner.setDataSource(dataSource);
        columnRangePartitioner.setTable("customer");

        return columnRangePartitioner;
    }

    @Bean
    @StepScope
    public ItemReader<Customer> pagingItemReader(@Value("#{stepExecutionContext['minValue']}") Long minValue,
                                                 @Value("#{stepExecutionContext['maxValue']}") Long maxValue) throws Exception {
        Map<String, Object> parameters = Map.of(
                "minValue", minValue,
                "maxValue", maxValue);

        return new JdbcPagingItemReaderBuilder<Customer>()
                .name("jdbcPagingItemReader")
                .pageSize(chunkSize)
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new CustomerRowMapper())
                .queryProvider(createQueryProvider())
                .parameterValues(parameters)
                .build();
    }

    private PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("id, firstname, lastname, birthdate");
        queryProvider.setFromClause("customer");
        queryProvider.setWhereClause("id between :minValue and :maxValue");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    @StepScope
    public ItemWriter<Customer> customItemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql("insert into customer2 values (:id, :firstname, :lastname, :birthdate)")
                .beanMapped()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);    // 기본 pool size
        executor.setMaxPoolSize(8);     // 최대 pool size
        executor.setThreadNamePrefix("async-thread-");
        return executor;
    }

}
