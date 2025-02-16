package io.spring.springbatchlecture.batch.job.listener;

import io.spring.springbatchlecture.batch.listener.CustomChunkListener;
import io.spring.springbatchlecture.batch.listener.CustomItemProcessListener2;
import io.spring.springbatchlecture.batch.listener.CustomItemReadListener2;
import io.spring.springbatchlecture.batch.listener.CustomItemWriteListener2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "spring.batch.job", name = "name", havingValue = "chunkListenerJob")
public class ChunkListenerJobConfig extends DefaultBatchConfiguration {

    private final int chunkSize = 5;

    @Bean
    public Job job(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("chunkListenerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step step1(final JobRepository jobRepository, final PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<Integer, String>chunk(chunkSize, transactionManager)
                .listener(new CustomChunkListener())
                .listener(new CustomItemReadListener2())
                .listener(new CustomItemProcessListener2())
                .listener(new CustomItemWriteListener2())
                .reader(listItemReader())
                .processor(item -> "item" + item)
                .writer(items -> System.out.println("items = " + items))
                .build();
    }

    @Bean
    public ListItemReader<Integer> listItemReader() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return new ListItemReader<>(list);
    }

}
