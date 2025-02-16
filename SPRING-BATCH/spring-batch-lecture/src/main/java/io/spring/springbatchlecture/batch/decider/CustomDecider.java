package io.spring.springbatchlecture.batch.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class CustomDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        int count = (int) (Math.random() * 100);
        
        return count % 2 == 0
                ? new FlowExecutionStatus("EVEN")
                : new FlowExecutionStatus("ODD");
    }

}
