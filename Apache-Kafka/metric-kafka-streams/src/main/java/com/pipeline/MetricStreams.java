package com.pipeline;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class MetricStreams {

    private static KafkaStreams streams;

    public static void main(final String[] args) {
        // 카프카 스트림즈의 안전한 종료를 위해 셧다운 훅을 받을 경우 close() 메서드를 호출하여 안전하게 종료한다.
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());

        // 스트림즈를 실행하기 위한 옵션들을 Properties 인스턴스에 정의
        // metric-streams-application 으로 애플리케이션 이름을 지정하고
        // 메시지 키와 메시지 값의 직렬화, 역직렬화 클래스는 String 타입으로 설정한다.
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "metric-streams-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // 카프카 스트림즈의 토폴로지를 정의하기 위해 StreamsBuilder 인스턴스를 새로 생성하고
        // 최초로 가져올 토픽인 metric.all 을 기반으로 한 KStream 인스턴스를 생성한다.
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> metrics = builder.stream("metric.all");

        // 메시지 값을 기준으로 분기처리하기 위해 MetricJsonUtils 를 통해
        // JSON 데이터에 적힌 메트릭 종류 값을 토대로 KStream 을 두 갈래로 분기한다.
        // branch() 메서드를 사용하고 내부 파라미터로 Predicate 함수형 인터페이스를 사용하면 분기처리할 수 있다.
        // KStream 의 0번 배열에는 CPU 데이터, 1번 배열에는 메모리 데이터가 들어가도록 설정했다.
        KStream<String, String>[] metricBranch = metrics.branch(    // deprecated 된 메서드이므로 수정 요망
                (key, value) -> MetricJsonUtils.getMetricName(value).equals("cpu"),
                (key, value) -> MetricJsonUtils.getMetricName(value).equals("memory"));

        metricBranch[0].to("metric.cpu");
        metricBranch[1].to("metric.memory");

        // 분기처리된 데이터 중 전체 CPU 사용량이 50%가 넘어갈 경우를 필터링하기 위해 filter() 메서드를 사용했다.
        KStream<String, String> filteredCpuMetric = metricBranch[0]
                .filter((key, value) -> MetricJsonUtils.getTotalCpuPercent(value) > 0.5);

        // 전체 CPU 사용량의 50%가 넘는 데이터의 메시지 값을 모두 전송하는 것이 아니라
        // 호스트 이름과 timestamp 값만 필요하므로 두 개의 값을 조합하는 MetricJsonUtils 의
        // getHostTimestamp() 메서드를 호출하여 변환된 형태로 받는다.
        // 이 데이터는 to() 메서드에 정의된 metric.cpu.alert 토픽으로 전달된다.
        filteredCpuMetric.mapValues(value -> MetricJsonUtils.getHostTimestamp(value)).to("metric.cpu.alert");

        // StreamBuilder 인스턴스로 정의된 토폴로지와 스트림즈 설정값을 토대로
        // KafkaStreams 인스턴스를 생성하고 실행한다.
        streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    // 카프카 스트림즈의 안전한 종료를 위해 셧다운 훅을 받을 경우 close() 메서드를 호출하여 안전하게 종료한다.
    static class ShutdownThread extends Thread {
        public void run() {
            streams.close();
        }
    }

}
