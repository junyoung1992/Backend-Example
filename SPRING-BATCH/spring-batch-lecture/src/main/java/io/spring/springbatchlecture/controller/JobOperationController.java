package io.spring.springbatchlecture.controller;

import io.spring.springbatchlecture.controller.dto.JobInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "operationJob")
public class JobOperationController {

    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;
    private final JobOperator jobOperator;

    @PostMapping("/batch/start")
    public String start(@RequestBody JobInfo jobInfo) throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {
        for (String jobName : jobRegistry.getJobNames()) {
            SimpleJob job = (SimpleJob) jobRegistry.getJob(jobName);
            System.out.println("jobName = " + job.getName());

            Properties properties = new Properties();
            properties.put("id", jobInfo.id());
            jobOperator.start(job.getName(), properties);
        }

        return "batch is started.";
    }

    @PostMapping("/batch/stop")
    public String stop() throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        for (String jobName : jobRegistry.getJobNames()) {
            SimpleJob job = (SimpleJob) jobRegistry.getJob(jobName);
            System.out.println("jobName = " + job.getName());

            Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(job.getName());
            for (JobExecution jobExecution : runningJobExecutions) {
                jobOperator.stop(jobExecution.getId());
            }
        }

        return "batch is started.";
    }

    @PostMapping("/batch/restart")
    public String restart() throws NoSuchJobException, NoSuchJobExecutionException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException {
        for (String jobName : jobRegistry.getJobNames()) {
            SimpleJob job = (SimpleJob) jobRegistry.getJob(jobName);
            System.out.println("jobName = " + job.getName());

            JobInstance lastJobInstance = jobExplorer.getLastJobInstance(jobName);
            if (lastJobInstance == null) {
                continue;
            }

            JobExecution lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);
            if (lastJobExecution == null) {
                continue;
            }

            jobOperator.restart(lastJobExecution.getId());
        }

        return "batch is started.";
    }

}
