package io.spring.springbatchlecture.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class PassCheckingListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        return !stepExecution.getExitStatus().equals(ExitStatus.FAILED)
                ? new ExitStatus("PASS")
                : stepExecution.getExitStatus();
    }

}
