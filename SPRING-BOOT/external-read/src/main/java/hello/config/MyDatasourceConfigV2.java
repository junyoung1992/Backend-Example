package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class MyDatasourceConfigV2 {

    private final MyDataSourcePropertiesV2 properties;

    public MyDatasourceConfigV2(MyDataSourcePropertiesV2 properties) {
        this.properties = properties;
    }

    @Bean
    public MyDataSource dataSource() {
        return new MyDataSource(properties.getUrl(), properties.getUsername(), properties.getPassword(), properties.getEtc().getMaxConnection(), properties.getEtc().getTimeout(), properties.getEtc().getOptions());
    }

}
