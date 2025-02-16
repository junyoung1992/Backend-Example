package io.springbatch.exercise.batch.job.api;

import io.springbatch.exercise.batch.chunk.processor.ApiItemProcessor1;
import io.springbatch.exercise.batch.chunk.processor.ApiItemProcessor2;
import io.springbatch.exercise.batch.chunk.processor.ApiItemProcessor3;
import io.springbatch.exercise.batch.chunk.writer.ApiItemWriter1;
import io.springbatch.exercise.batch.chunk.writer.ApiItemWriter2;
import io.springbatch.exercise.batch.chunk.writer.ApiItemWriter3;
import io.springbatch.exercise.batch.classifier.ProcessorClassifier;
import io.springbatch.exercise.batch.classifier.WriterClassifier;
import io.springbatch.exercise.batch.domain.ApiRequestVO;
import io.springbatch.exercise.batch.domain.ProductVO;
import io.springbatch.exercise.batch.partition.ProductPartitioner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration extends DefaultBatchConfiguration {

    private final DataSource dataSource;

    private final int chunkSize = 10;
    private final int gridSize = 3;

    @Bean
    public Step apiMasterStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("apiMasterStep", jobRepository)
                .partitioner(apiSlaveStep(jobRepository, transactionManager).getName(), partitioner())
                .step(apiSlaveStep(jobRepository, transactionManager))
                .gridSize(gridSize)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setThreadNamePrefix("api-thread-");

        return taskExecutor;
    }

    @Bean
    public Step apiSlaveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("apiSlaveStep", jobRepository)
                .<ProductVO, ApiRequestVO>chunk(chunkSize, transactionManager)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Partitioner partitioner() {
        return new ProductPartitioner(dataSource);
    }

    @Bean
    @StepScope
    public ItemStreamReader<? extends ProductVO> itemReader(@Value("#{stepExecutionContext['productType']}") String productType) throws Exception {
        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVO.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("product");
        queryProvider.setWhereClause("type = :type");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productType));
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemProcessor<? super ProductVO, ? extends ApiRequestVO> itemProcessor() {
        ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor = new ClassifierCompositeItemProcessor<>();
        ProcessorClassifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> classifier = new ProcessorClassifier<>();

        Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();
        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());

        classifier.setProcessorMap(processorMap);
        processor.setClassifier(classifier);

        return processor;
    }

    @Bean
    public ItemWriter<? super ApiRequestVO> itemWriter() {
        ClassifierCompositeItemWriter<ApiRequestVO> writer = new ClassifierCompositeItemWriter<>();
        WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier = new WriterClassifier<>();

        Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();
        writerMap.put("1", new ApiItemWriter1());
        writerMap.put("2", new ApiItemWriter2());
        writerMap.put("3", new ApiItemWriter3());

        classifier.setWriterMap(writerMap);
        writer.setClassifier(classifier);

        return writer;
    }

}
