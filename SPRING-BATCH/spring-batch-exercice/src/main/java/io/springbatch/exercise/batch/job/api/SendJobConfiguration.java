package io.springbatch.exercise.batch.job.api;

import io.springbatch.exercise.batch.listener.JobListener;
import io.springbatch.exercise.batch.tasklet.ApiEndTasklet;
import io.springbatch.exercise.batch.tasklet.ApiStartTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SendJobConfiguration {

    private final ApiStartTasklet apiStartTasklet;
    private final ApiEndTasklet apiEndTasklet;
    private final Step jobStep;

    @Bean
    public Job apiJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("apiJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobListener())
                .start(apiStep1(jobRepository, transactionManager))
                .next(jobStep)
                .next(apiStep2(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step apiStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("apiStep1", jobRepository)
                .tasklet(apiStartTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step apiStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("apiStep2", jobRepository)
                .tasklet(apiEndTasklet, transactionManager)
                .build();
    }

}
