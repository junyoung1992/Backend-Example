package io.spring.springbatchlecture.batch.job.base;

import io.spring.springbatchlecture.batch.tasklet.CustomTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "parentJob")
public class ParentJobConfig extends DefaultBatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job parentJob() {
        return new JobBuilder("parentJob", jobRepository)
                .start(jobStep0(null))
                .next(parentJobStep2())
                .build();
    }

    @Bean
    public Step jobStep0(final JobLauncher jobLauncher) {
        return new StepBuilder("jobStep", jobRepository)
                .job(childJob())
                .launcher(jobLauncher)
                .parametersExtractor(jobParametersExtractor())
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        stepExecution.getExecutionContext().putString("name", "user1");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        return StepExecutionListener.super.afterStep(stepExecution);
                    }
                })
                .build();
    }

    @Bean
    public Job childJob() {
        return new JobBuilder("childJob", jobRepository)
                .start(childJobStep1())
                .build();
    }

    @Bean
    public Step childJobStep1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    private JobParametersExtractor jobParametersExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"name"});
        return extractor;
    }

    @Bean
    public Step parentJobStep2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

}
