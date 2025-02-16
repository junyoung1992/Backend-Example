package io.spring.springbatchlecture.batch.job.base;

import io.spring.springbatchlecture.batch.tasklet.CustomTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "job")
public class JobConfig extends DefaultBatchConfiguration {

//    private final JobRepositoryListener jobRepositoryListener;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("job", jobRepository)
//                .incrementer(new CustomJobParametersIncrementer())
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .next(step2(jobRepository, transactionManager))
                .next(step3(jobRepository, transactionManager))
//                .validator(new CustomJobParametersValidator())
//                .validator(new DefaultJobParametersValidator(new String[]{"name", "date"}, new String[]{"count"}))
//                .listener(jobRepositoryListener)
//                .preventRestart()
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step step2(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .allowStartIfComplete(true) // completed 상태여도 재시작시 무조건 실행
                .build();
    }

    @Bean
    public Step step3(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step3", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .startLimit(10) // 실행 반복 횟수
                .build();
    }

}
