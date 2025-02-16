package io.spring.springbatchlecture.batch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        System.out.println("stepName =  " + stepName);

        stepExecution.getExecutionContext().put("name", "user1");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String name = (String) stepExecution.getExecutionContext().get("name");
        System.out.println("name = " + name);

        ExitStatus exitStatus = stepExecution.getExitStatus();
        System.out.println("exitStatus = " + exitStatus.getExitCode());

        BatchStatus status = stepExecution.getStatus();
        System.out.println("status = " + status.name());

        return ExitStatus.COMPLETED;
    }

}
