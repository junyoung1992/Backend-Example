package io.spring.springbatchlecture.batch.job.base;

import io.spring.springbatchlecture.batch.tasklet.CustomTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "stepBuilderJob")
public class StepBuilderJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job stepBuilderJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("stepBuilderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stepBuilderStep1(jobRepository, transactionManager))
                .next(stepBuilderStep2(jobRepository, transactionManager))
//                .next(stepBuilderStep3(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step stepBuilderStep1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step stepBuilderStep2(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepBuilderStep2", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))
                .processor(String::toUpperCase)
                .writer(item -> {
                    item.forEach(System.out::println);
                })
                .build();
    }

    @Bean
    public Step stepBuilderStep3(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepBuilderStep3", jobRepository)
                .partitioner(stepBuilderStep1(jobRepository, transactionManager))
                .gridSize(2)
                .build();
    }

    @Bean
    public Step stepBuilderStep4(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepBuilderStep4", jobRepository)
                .job(stepBuilderSubJob(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step stepBuilderStep5(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepBuilderStep5", jobRepository)
                .flow(stepBuilderSubFlow(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Job stepBuilderSubJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("stepBuilderSubJob", jobRepository)
                .start(stepBuilderStep1(jobRepository, transactionManager))
                .next(stepBuilderStep2(jobRepository, transactionManager))
                .next(stepBuilderStep3(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Flow stepBuilderSubFlow(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new FlowBuilder<Flow>("stepBuilderSubFlow")
                .start(stepBuilderStep2(jobRepository, transactionManager))
                .end();
    }

}
