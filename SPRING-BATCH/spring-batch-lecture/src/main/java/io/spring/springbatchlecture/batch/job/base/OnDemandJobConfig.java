package io.spring.springbatchlecture.batch.job.base;

import io.spring.springbatchlecture.batch.tasklet.CustomTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnProperty(prefix = "spring.batch.job", name = "test", havingValue = "onDemandJob")
public class OnDemandJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job onDemandJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("onDemandJob", jobRepository)
                .start(onDemandJobStep1(jobRepository, transactionManager))
                .next(onDemandJobStep2(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step onDemandJobStep1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    Thread.sleep(3000);
                    System.out.println("step1 was executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step onDemandJobStep2(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

}
