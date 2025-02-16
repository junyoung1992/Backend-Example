package io.spring.springbatchlecture.batch.job.retry;

import io.spring.springbatchlecture.batch.processor.SkipItemProcessor;
import io.spring.springbatchlecture.batch.writer.SkipItemWriter;
import io.spring.springbatchlecture.exception.SkippableException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "skipJob")
public class SkipJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("skipJob", jobRepository)
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
                    public String read() throws SkippableException {
                        i++;

                        if (i == 3) {
                            throw new SkippableException("This exception is skipped.");
                        }

                        System.out.println("ItemReader : " + i);
                        return i > 20 ? null : String.valueOf(i);
                    }
                })
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .faultTolerant()
//                .skip(SkippableException.class)
//                .noSkip(NoSkippableException.class)
//                .skipLimit(2)
                .skipPolicy(limitCheckingItemSkipPolicy())
                .build();
    }

    @Bean
    public ItemProcessor<? super String, ? extends String> customItemProcessor() {
        return new SkipItemProcessor();
    }

    @Bean
    public ItemWriter<? super String> customItemWriter() {
        return new SkipItemWriter();
    }

    @Bean
    public SkipPolicy limitCheckingItemSkipPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionClasses = new HashMap<>();
        exceptionClasses.put(SkippableException.class, true);

        return new LimitCheckingItemSkipPolicy(4, exceptionClasses);
    }

}
