package io.spring.springbatchlecture.batch.job.multiThread;

import io.spring.springbatchlecture.entity.Customer;
import io.spring.springbatchlecture.mapper.CustomerRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "synchronizedJobConfig")
public class SynchronizedJobConfig extends DefaultBatchConfiguration {

    private final DataSource dataSource;
    private final int chunkSize = 10;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("synchronizedJobConfig", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Customer, Customer>chunk(chunkSize, transactionManager)
                .reader(threadSafetyItemReader())
                .listener(new ItemReadListener<>() {
                    @Override
                    public void afterRead(Customer item) {
                        System.out.println("item.getId() : " + item.getId());
                    }
                })
                .writer(customItemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public ItemStreamReader<Customer> notThreadSafetyItemReader() {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("NotThreadSafetyReader")
                .dataSource(dataSource)
                .fetchSize(chunkSize)
                .sql("""
                        select id
                             , firstname
                             , lastname
                             , birthdate
                        from customer
                        """)
                .rowMapper(new CustomerRowMapper())
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<Customer> threadSafetyItemReader() {
        ItemStreamReader<Customer> notThreadSafetyReader = new JdbcCursorItemReaderBuilder<Customer>()
                .name("NotThreadSafetyReader")
                .dataSource(dataSource)
                .fetchSize(chunkSize)
                .sql("""
                        select id
                             , firstname
                             , lastname
                             , birthdate
                        from customer
                        """)
                .rowMapper(new CustomerRowMapper())
                .build();

        return new SynchronizedItemStreamReaderBuilder<Customer>()
                .delegate(notThreadSafetyReader)
                .build();
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
