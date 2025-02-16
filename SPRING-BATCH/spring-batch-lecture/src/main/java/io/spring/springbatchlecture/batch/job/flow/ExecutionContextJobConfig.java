package io.spring.springbatchlecture.batch.job.flow;

import io.spring.springbatchlecture.batch.tasklet.ExecutionContextTasklet1;
import io.spring.springbatchlecture.batch.tasklet.ExecutionContextTasklet2;
import io.spring.springbatchlecture.batch.tasklet.ExecutionContextTasklet3;
import io.spring.springbatchlecture.batch.tasklet.ExecutionContextTasklet4;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "executionContextJob")
public class ExecutionContextJobConfig extends DefaultBatchConfiguration {

    private final ExecutionContextTasklet1 executionContextTasklet1;
    private final ExecutionContextTasklet2 executionContextTasklet2;
    private final ExecutionContextTasklet3 executionContextTasklet3;
    private final ExecutionContextTasklet4 executionContextTasklet4;

    @Bean
    public Job executionContextJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("executionContextJob", jobRepository)
                .start(executionContextStep1(jobRepository, transactionManager))
                .next(executionContextStep2(jobRepository, transactionManager))
                .next(executionContextStep3(jobRepository, transactionManager))
                .next(executionContextStep4(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step executionContextStep1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(executionContextTasklet1, transactionManager)
                .build();
    }

    @Bean
    public Step executionContextStep2(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(executionContextTasklet2, transactionManager)
                .build();
    }

    @Bean
    public Step executionContextStep3(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .tasklet(executionContextTasklet3, transactionManager)
                .build();
    }

    @Bean
    public Step executionContextStep4(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step4", jobRepository)
                .tasklet(executionContextTasklet4, transactionManager)
                .build();
    }

}
