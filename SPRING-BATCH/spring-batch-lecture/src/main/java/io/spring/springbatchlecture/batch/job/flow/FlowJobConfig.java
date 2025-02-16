package io.spring.springbatchlecture.batch.job.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "flowJob")
public class FlowJobConfig extends DefaultBatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job flowJob() {
        return new JobBuilder("flowJob", jobRepository)
                .start(flowA())
//                .next(flowStep3())
//                .next(flowB())
//                .next(flowStep6())
                .end()
                .build();
    }

    @Bean
    public Flow flowA() {
        return new FlowBuilder<Flow>("flowA")
                .start(flowStep1())
                .on("FAILED").to(flowStep2())
                .on("FAILED").stop()
                .from(flowStep1())
                .on("*").to(flowStep3())
                .next(flowStep4())
                .from(flowStep2())
                .on("*").to(flowStep5())
                .end();
    }

    @Bean
    public Flow flowB() {
        return new FlowBuilder<Flow>("flowB")
                .start(flowStep4())
                .next(flowStep5())
                .end();
    }

    @Bean
    public Step flowStep1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1 was executed.");
                    contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);   // BatchStatus을 기준으로 분기
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowStep2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 was executed.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowStep3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step3 was executed.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowStep4() {
        return new StepBuilder("step4", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step4 was executed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowStep5() {
        return new StepBuilder("step5", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step5 was executed.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowStep6() {
        return new StepBuilder("step6", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step6 was executed.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
