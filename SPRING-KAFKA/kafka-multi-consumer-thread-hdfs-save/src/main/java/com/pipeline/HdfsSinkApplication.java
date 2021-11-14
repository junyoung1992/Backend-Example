package com.pipeline;

import com.pipeline.consumer.ConsumerWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class HdfsSinkApplication {

    // kafka 클러스터 정보, 토픽 이름, 컨슈머 그룹 설정
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";
    private final static String TOPIC_NAME = "select-color";
    private final static String GROUP_ID = "color-hdfs-save-consumer-group";

    // 컨슈머 스레드 설정
    private final static int CONSUMER_COUNT = 3;

    private final static List<ConsumerWorker> workers = new ArrayList<>();

    public static void main(String[] args) {
        // 안전한 컨슈머 종료를 위해 ShutdownHook 선언
        // ShutdownHook 이 발생했을 경우 각 컨슈머 스레드에 종료를 알리도록 명시적으로 stopAndWakeup() 메서드 호출
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

        // 컨슈머 설정 선언
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // 컨슈머 스레드를 스레드 풀로 관리하기 위해 newCacheThreadPool() 생성
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            workers.add(new ConsumerWorker(configs, TOPIC_NAME, i));
        }

        // execute() 메서드로 컨슈머 스레드 인스턴스들을 스레트 풀에 포함시켜 실행
        workers.forEach(executorService::execute);

    }

    private static class ShutdownThread extends Thread {
        public void run() {
            log.info("Shutdown hook");
            workers.forEach(ConsumerWorker::stopAndWakeup);
        }
    }
}
