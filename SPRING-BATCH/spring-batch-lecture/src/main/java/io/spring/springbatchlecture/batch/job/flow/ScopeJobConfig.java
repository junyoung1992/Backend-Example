package io.spring.springbatchlecture.batch.job.flow;

import io.spring.springbatchlecture.batch.listener.ScopeJobListener;
import io.spring.springbatchlecture.batch.listener.ScopeJobStepListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "scopeJob")
public class ScopeJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job scopeJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("scopeJob", jobRepository)
                .start(scopeJobStep1(jobRepository, transactionManager, null))
                .next(scopeJobStep2(jobRepository, transactionManager))
                .listener(new ScopeJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step scopeJobStep1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager,
                              @Value("#{jobParameters['message']}") String message) {
        System.out.println("message = " + message);

        return new StepBuilder("step1", jobRepository)
                .tasklet(scopeJobTasklet1(null), transactionManager)
                .build();
    }

    @Bean
    public Step scopeJobStep2(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(scopeJobTasklet2(null), transactionManager)
                .listener(new ScopeJobStepListener())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet scopeJobTasklet1(@Value("#{jobExecutionContext['name']}") String name) {
        System.out.println("name = " + name);

        return (stepContribution, chunkContribution) -> {
            System.out.println("tasklet1 was executed");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public Tasklet scopeJobTasklet2(@Value("#{stepExecutionContext['name2']}") String name2) {
        System.out.println("name2 = " + name2);

        return (stepContribution, chunkContribution) -> {
            System.out.println("tasklet2 was executed");
            return RepeatStatus.FINISHED;
        };
    }

}
