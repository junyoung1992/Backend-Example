package io.spring.springbatchlecture.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class CustomBatchConfig {

    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public JobRepository customJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();

        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factory.setTablePrefix("SYSTEM_");
        factory.setIncrementerFactory(new DefaultDataFieldMaxValueIncrementerFactory(dataSource));
        factory.afterPropertiesSet();

        return factory.getObject();
    }

}
