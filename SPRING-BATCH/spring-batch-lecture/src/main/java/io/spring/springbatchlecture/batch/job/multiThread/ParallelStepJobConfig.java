package io.spring.springbatchlecture.batch.job.multiThread;

import io.spring.springbatchlecture.batch.listener.StopWatchJobListener;
import io.spring.springbatchlecture.batch.tasklet.CustomTasklet2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "parallelStepJob")
public class ParallelStepJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("parallelStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(flow1(jobRepository, transactionManager))
                .split(taskExecutor())
                .add(flow2(jobRepository, transactionManager))
                .end()
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Flow flow1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        TaskletStep step1 = new StepBuilder("step1", jobRepository)
                .tasklet(tasklet(), transactionManager)
                .build();

        return new FlowBuilder<Flow>("flow1")
                .start(step1)
                .end();
    }

    @Bean
    public Flow flow2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        TaskletStep step2 = new StepBuilder("step2", jobRepository)
                .tasklet(tasklet(), transactionManager)
                .build();

        TaskletStep step3 = new StepBuilder("step3", jobRepository)
                .tasklet(tasklet(), transactionManager)
                .build();

        return new FlowBuilder<Flow>("flow2")
                .start(step2)
                .next(step3)
                .end();
    }

    @Bean
    public Tasklet tasklet() {
        return new CustomTasklet2();
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
