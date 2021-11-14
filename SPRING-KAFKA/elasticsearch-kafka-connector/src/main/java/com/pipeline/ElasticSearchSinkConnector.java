package com.pipeline;

import com.pipeline.config.ElasticSearchSinkConnectorConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ElasticSearchSinkConnector 클래스는 커넥터를 생성했을 때 최초로 실행된다.<br>
 * SinkConnector 추상클래스를 상속받은 ElasticSearchSinkConnector 는<br>
 * version(), start(), taskClass(), taskConfigs(), config(), stop() 총 6개 메서드를 구현해야 한다.<br>
 * ElasticSearchSinkConnector 는 직접적으로 데이터를 적재하는 로직을 포함하는 것은 아니고,<br>
 * 태스크를 실행하기 위한 이전 단계로써 설정값을 확인하고 태스크 클래스를 지정하는 역할을 수행한다.
 */

@Slf4j
public class ElasticSearchSinkConnector extends SinkConnector {

    private Map<String, String> configProperties;

    @Override
    // 커넥터 버전 설정
    // 여기서 설정한 버전은 커넥터의 버전에 따른 변경사항을 확인하기 위해 versioning 할 때 필요
    public String version() {
        return "1.0";
    }

    @Override
    // 커넥터가 최초로 실행될 때 실행되는 구문
    // 사용자로부터 설정값을 가져와서 ElasticSearchSinkConnectorConfig 인스턴스를 생성
    // 정상적이지 않은 설정값일 경우 ConfigException 발생
    public void start(Map<String, String> props) {
        this.configProperties = props;

        try {
            new ElasticSearchSinkConnectorConfig(props);
        } catch (ConfigException e) {
            throw new ConnectException(e.getMessage(), e);
        }
    }

    @Override
    // 커넥터를 실행했을 경우 태스크 역할을 할 클래스를 선언
    // 만약 다수의 태스크를 운영할 경우 태스크 클래스 분기 로직을 넣을 수 있다.
    // 타깃 애플리케이션의 버전에 맞는 로직을 담은 태스크 클래스들을 두 개 이상 만드는 경우
    // 사용자가 원하는 태스크 클래스를 taskClass() 메서드에서 선택할 수 있다.
    public Class<? extends Task> taskClass() {
        return ElasticSearchSinkTask.class;
    }

    @Override
    // 태스크별로 다른 설정값을 부여할 경우 여기에 로직을 넣을 수 있다.
    // 여기서는 모든 태스크에 동일한 설정값을 설정한다.
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        Map<String, String> taskProps = new HashMap<>();
        taskProps.putAll(configProperties);

        List<Map<String, String>> taskConfigs = new ArrayList<>();
        for (int i = 0; i < maxTasks; i++) {
            taskConfigs.add(taskProps);
        }

        return taskConfigs;
    }

    @Override
    // 커넥터가 종료될 때 로그를 남긴다.
    public void stop() {
        log.info("Stop elasticsearch connector");
    }

    @Override
    // ElasticSearchSinkConnectorConfig 에서 설정한 설정값을 리턴한다.
    // 리턴한 설정값은 사용자가 커넥터를 생성할 때 설정값을 정상적으로 입력했는지 검증할 때 사용한다.
    public ConfigDef config() {
        return ElasticSearchSinkConnectorConfig.CONFIG;
    }
}
