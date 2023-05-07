package hello;

import hello.config.MyDatasourceConfigV3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@Import(MyDatasourceEnvConfig.class)
//@Import(MyDatasourceValueConfig.class)
//@Import(MyDatasourceConfigV1.class)
//@Import(MyDatasourceConfigV2.class)
@Import(MyDatasourceConfigV3.class)
@SpringBootApplication(scanBasePackages = {"hello.datasource", "hello.pay"})
//@ConfigurationPropertiesScan
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}
