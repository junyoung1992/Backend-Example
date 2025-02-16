package io.spring.springbatchlecture.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomTasklet2 implements Tasklet {

    private final Object lock = new Object();

    private long sum = 0L;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

//        for (int i = 0; i < 1_000_000_000; i++) {
//            sum++;
//        }

        long localSum = 0;
        for (int i = 0; i < 1_000_000_000; i++) {
            localSum++;
        }

        synchronized (lock) {
            sum += localSum;
        }

        System.out.printf("""
                        %s has been executed on thread %s
                        sum : %d
                        """,
                chunkContext.getStepContext().getStepName(), Thread.currentThread().getName(), sum);

        return RepeatStatus.FINISHED;
    }

}
