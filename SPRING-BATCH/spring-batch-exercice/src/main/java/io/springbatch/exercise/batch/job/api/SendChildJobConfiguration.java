package io.springbatch.exercise.batch.job.api;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SendChildJobConfiguration {

    private final Step apiMasterStep;
    private final JobLauncher jobLauncher;

    @Bean
    public Step jobStep(JobRepository jobRepository) {
        return new StepBuilder("jobStep", jobRepository)
                .job(childJob(jobRepository))
                .launcher(jobLauncher)
                .build();
    }

    @Bean
    public Job childJob(JobRepository jobRepository) {
        return new JobBuilder("childJob", jobRepository)
                .start(apiMasterStep)
                .build();
    }

}
