package io.spring.springbatchlecture.batch.job.chunk;

import io.spring.springbatchlecture.service.CustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "itemReaderAdapterJob")
public class ItemReaderAdapterJobConfig extends DefaultBatchConfiguration {

    private final int chunkSize = 5;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new JobBuilder("itemReaderAdapterJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(chunkSize, transactionManager)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemReader<String> customItemReader() {
        ItemReaderAdapter<String> reader = new ItemReaderAdapter<>();
        reader.setTargetObject(customService());
        reader.setTargetMethod("customRead");

        return reader;
    }

    @Bean
    public Object customService() {
        return new CustomService<String>();
    }

    @Bean
    public ItemWriter<String> customItemWriter() {
        return items -> {
            for (String item : items) {
                System.out.println(item);
            }
        };
    }

}
