package io.spring.springbatchlecture.batch.job.multiThread;

import io.spring.springbatchlecture.batch.listener.CustomItemProcessListener;
import io.spring.springbatchlecture.batch.listener.CustomItemReadListener;
import io.spring.springbatchlecture.batch.listener.CustomItemWriteListener;
import io.spring.springbatchlecture.batch.listener.StopWatchJobListener;
import io.spring.springbatchlecture.entity.Customer;
import io.spring.springbatchlecture.mapper.CustomerRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
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
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "multiThreadStepJob")
public class MultiThreadStepJobConfig extends DefaultBatchConfiguration {

    private final DataSource dataSource;
    private final int chunkSize = 5;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("multiThreadStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(pagingItemReader())
                .listener(new CustomItemReadListener())
                .processor(customItemProcessor())
                .listener(new CustomItemProcessListener())
                .writer(customItemWriter())
                .listener(new CustomItemWriteListener())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public ItemReader<Customer> pagingItemReader() throws Exception {
        Map<String, Object> parameters = Map.of("firstname", "A%");

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
//        queryProvider.setWhereClause("firstname like :firstname");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<Customer, Customer> customItemProcessor() {
        return item -> {
            Thread.sleep(1000);
            return new Customer(item.getId(),
                    item.getFirstname().toUpperCase(),
                    item.getLastname().toUpperCase(),
                    item.getBirthdate(),
                    null,
                    null);
        };
    }

    @Bean
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
