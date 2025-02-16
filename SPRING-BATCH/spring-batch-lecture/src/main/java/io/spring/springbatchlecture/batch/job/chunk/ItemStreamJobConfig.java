package io.spring.springbatchlecture.batch.job.chunk;

import io.spring.springbatchlecture.batch.reader.CustomItemStreamReader;
import io.spring.springbatchlecture.batch.writer.CustomItemStreamWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "itemStreamJob")
public class ItemStreamJobConfig extends DefaultBatchConfiguration {

    @Bean
    public Job itemStreamJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("itemStreamJob", jobRepository)
                .start(itemStreamJobStep1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step itemStreamJobStep1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(5, transactionManager)
                .reader(customItemStreamReader())
                .writer(customItemStreamWriter())
                .build();
    }

    @Bean
    public CustomItemStreamReader customItemStreamReader() {
        List<String> items = IntStream.rangeClosed(0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());

        return new CustomItemStreamReader(items);
    }

    @Bean
    public ItemWriter<? super String> customItemStreamWriter() {
        return new CustomItemStreamWriter();
    }

}
