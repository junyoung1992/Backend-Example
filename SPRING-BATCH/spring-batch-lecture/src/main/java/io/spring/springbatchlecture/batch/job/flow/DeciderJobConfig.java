package io.spring.springbatchlecture.batch.job.flow;

import io.spring.springbatchlecture.batch.decider.CustomDecider;
import io.spring.springbatchlecture.batch.listener.PassCheckingListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
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
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "deciderJob")
public class DeciderJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job deciderJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("deciderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(deciderStep1(jobRepository, transactionManager))
                .next(decider())
                .from(decider()).on("ODD").to(oddStep(jobRepository, transactionManager))
                .from(decider()).on("EVEN").to(evenStep(jobRepository, transactionManager))
                .end()
                .build();
    }

    @Bean
    public Step deciderStep1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step was executed.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new CustomDecider();
    }

    @Bean
    public Step oddStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("oddStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("oddStep was executed.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .listener(new PassCheckingListener())
                .build();
    }

    @Bean
    public Step evenStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("evenStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("evenStep was executed.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .listener(new PassCheckingListener())
                .build();
    }

}
