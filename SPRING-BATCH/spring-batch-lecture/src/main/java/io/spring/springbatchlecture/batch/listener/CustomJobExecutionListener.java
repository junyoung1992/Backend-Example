package io.spring.springbatchlecture.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.temporal.ChronoUnit;

public class CustomJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job is started.");
        System.out.println("jobName = " + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long time = ChronoUnit.MILLIS.between(jobExecution.getStartTime(), jobExecution.getEndTime());
        System.out.println("총 소요시간 : " + time);
    }

}
