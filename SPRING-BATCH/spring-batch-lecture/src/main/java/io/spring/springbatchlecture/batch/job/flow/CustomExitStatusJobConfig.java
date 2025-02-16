package io.spring.springbatchlecture.batch.job.flow;

import io.spring.springbatchlecture.batch.listener.PassCheckingListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "customExitStatusJob")
public class CustomExitStatusJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job customExitStatusJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("customExitStatusJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(customExitStatusStep1(jobRepository, transactionManager))
                .on("FAILED").to(customExitStatusStep2(jobRepository, transactionManager))
                .on("PASS").stop()
                .end()
                .build();
    }

    @Bean
    public Step customExitStatusStep1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1 was executed.");
                    contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step customExitStatusStep2(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 was executed.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .listener(new PassCheckingListener())
                .build();
    }

}
