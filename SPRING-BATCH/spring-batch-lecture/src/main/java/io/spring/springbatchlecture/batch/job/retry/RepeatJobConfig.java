package io.spring.springbatchlecture.batch.job.retry;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "repeatJob")
public class RepeatJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("repeatJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(5, transactionManager)
                .reader(new ItemReader<>() {
                    int i = 0;

                    @Override
                    public String read() {
                        i++;
                        return i > 3 ? null : "item" + i;
                    }
                })
                .processor(new ItemProcessor<>() {
                    final RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) {
                        System.out.println("ItemProcessed: " + item);

                        // 세 번의 반복 이후 종료
//                        repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));

                        // 3초 이후 종료
//                        repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));

                        // 세 번 또는 3초
//                        CompositeCompletionPolicy completionPolicy = new CompositeCompletionPolicy();
//                        CompletionPolicy[] completionPolicies = new CompletionPolicy[]{
//                                new SimpleCompletionPolicy(3),
//                                new TimeoutTerminationPolicy(3000)};
//
//                        completionPolicy.setPolicies(completionPolicies);
//                        repeatTemplate.setCompletionPolicy(completionPolicy);

                        // 3회 예외까지는 반복 지속
                        repeatTemplate.setExceptionHandler(simpleLimitExceptionHandler());

                        repeatTemplate.iterate(context -> {
                            System.out.println("RepeatTemplate is testing.");
                            throw new RuntimeException("Exception is occurred.");
//                            return RepeatStatus.CONTINUABLE;
                        });

                        return item;
                    }
                })
                .writer(System.out::println)
                .build();
    }

    @Bean
    public SimpleLimitExceptionHandler simpleLimitExceptionHandler() {
        return new SimpleLimitExceptionHandler(3);
    }

}
