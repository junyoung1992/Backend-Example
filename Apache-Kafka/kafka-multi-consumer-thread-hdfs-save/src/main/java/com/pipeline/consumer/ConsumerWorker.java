package com.pipeline.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
// 컨슈머가 실행될 클래스를 정의하기 위해 Runnable 인터페이스로 클래스 정의
public class ConsumerWorker implements Runnable {

    /* 컨슈머의 poll() 메서드를 통해 전달받은 데이터를 임시 저장하는 버퍼를 bufferString 으로 선언
     * static 변수로 선언하여 다수의 스레드가 만들어지더라도 동일 변수에 접근함
     * 다수의 스레드가 동시에 접근할 수 있는 변수이므로 멀티 스레드 환경에서 한전하게 사용할 수 있도록
     * ConcurrentHashMap<> 으로 구현
     * bufferString 에는 파티션 번호와 메시지 값이 저장됨
     * currentFileOffset 은 오프셋 값을 저장하고, 파일 이름을 저장할 때 오프셋 번호를 붙일 때 사용됨
     */
    private static final Map<Integer, List<String>> bufferString = new ConcurrentHashMap<>();
    private static final Map<Integer, Long> currentFileOffset = new ConcurrentHashMap<>();

    private final static int FLUSH_RECORD_COUNT = 10;
    private final Properties prop;
    private final String topic;
    private final String threadName;
    private KafkaConsumer<String, String> consumer;

    public ConsumerWorker(Properties prop, String topic, int number) {
        log.info("Generate ConsumerWorker");

        this.prop = prop;
        this.topic = topic;
        this.threadName = "consumer-thread-" + number;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(threadName);

        // 스레드를 생성하는 HdfsSinkApplication 에서 설정한 컨슈머 설정을 가져와서
        // 카프카 컨슈머 인스턴스를 생성하고 토픽을 구독
        consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(List.of(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                // poll() 메서드로 가져온 데이터들을 버퍼에 쌓기 위해 addHdfsFileBuffer() 메서드를 호출
                for (ConsumerRecord<String, String> record : records) {
                    addHdfsFileBuffer(record);
                }

                // 한 번 polling 이 완료되고 버퍼에 쌓이이면 saveBufferToHdfsFile() 을 호출하여
                // 일정 개수 이상 버퍼에 데이터가 쌓였을 때 HDFS 에 저장하는 로직을 수행
                // 여기서 consumer.assignment() 값을 파라미터로 넘겨 주는 이유는
                // 현재 컨슈머 스레드에 할당된 파티션에 대한 버퍼 데이터만 적재하기 위함
                saveBufferToHdfsFile(consumer.assignment());
            }
        } catch (WakeupException e) {
            // 안전한 종료 위해 WakeupException 을 받아 컨슈머 종료 과정을 수행
            log.warn("Wakeup consumer");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            consumer.close();
        }
    }

    // 셧다운 훅이 발생했을 때 안전한 종료를 위해 컨슈머에 wakeup() 메서드를 호출
    // 남은 버퍼의 데이터를 모두 저장하기 위해 saveRemainBufferToHdfsFile() 메서드도 호출
    public void stopAndWakeup() {
        log.info("stopAndWakeup");

        consumer.wakeup();
        saveRemainBufferToHdfsFile();
    }

    // 레코드를 받아서 메시지 값을 버퍼에 넣는 코드
    // 만약 버퍼의 크기가 1 이면 버퍼의 가장 처음 오프셋이라는 뜻으로 currentFileOffset 변수에 추가
    // currentFileOffset 변수에서 오프셋 번호를 관리하면 추후 파일을 저장할 때
    // 파티션 이름과 오프셋 번호를 붙여 저장할 수 있기 때문에
    // 이슈 발생 시 파티션과 오프셋에 대한 정보를 알 수 있는 장점이 있음
    private void addHdfsFileBuffer(ConsumerRecord<String, String> record) {
        List<String> buffer = bufferString.getOrDefault(record.partition(), new ArrayList<>());
        buffer.add(record.value());
        bufferString.put(record.partition(), buffer);

        if (buffer.size() == 1) {
            currentFileOffset.put(record.partition(), record.offset());
        }
    }

    // 버퍼의 데이터가 flush 될 만큼인지 확인하는 checkFlushCount 메서드를 호출
    // 컨슈머로부터 Set<TopicPartition> 정보를 받아서 컨슈머 스레드에 할당된 파티션에만 접근
    private void saveBufferToHdfsFile(Set<TopicPartition> partitions) {
        partitions.forEach(p -> checkFlushCount(p.partition()));
    }

    // 파티션 번호의 버퍼를 확인하여 flush 를 수행할 만큼 레코드가 찼는지 확인
    // 만약 일정 개수 이상이면 HDFS 적재 로직인 save() 메서드 호출
    private void checkFlushCount(int partitionNo) {
        if (bufferString.get(partitionNo) != null) {
            if (bufferString.get(partitionNo).size() > FLUSH_RECORD_COUNT - 1) {
                save(partitionNo);
            }
        }
    }

    // 실질적인 HDFS 적재를 수행하는 메서드
    private void save(int partitionNo) {
        if (bufferString.get(partitionNo).size() > 0) {
            try {
                // HDFS 에 저장할 파일 이름은 color-{파티션 번호}-{오프셋번호}.log
                // 이렇게 저장해두면 파티션 번호, 오프셋 번호를 파일 이름만 보고도 바로 알 수 있기 때문에
                // 컨슈머 장애 시 복구 시점을 명확히 알 수 있음
                String fileName = "/data/color-" + partitionNo + "-" + currentFileOffset.get(partitionNo) + ".log";

                // HDFS 적재를 위한 설정을 수행
                // 로컬 개발환경에 HDFS 를 설치했으므로 hdfsL//localhost:9000 을 대상으로 FileSystem 인스턴스 생성
                Configuration configuration = new Configuration();
                configuration.set("fs.defaultFS", "hdfs://localhost:9000");
                FileSystem hdfsFileSystem = FileSystem.get(configuration);

                // 버퍼 데이터를 파일로 저장하기 위해 FSDataOutputStream 인스턴스 생성
                // (HDFS 에 데이터를 파일로 저장하기 위해 자바의 DataOutputStream 클래스를 상속받는 클래스)
                // bufferString 에 쌓인 버퍼 데이터를 fileOutputStream 에 저장
                FSDataOutputStream fileOutputStream = hdfsFileSystem.create(new Path(fileName));
                fileOutputStream.writeBytes(StringUtils.join(bufferString.get(partitionNo), "\n"));
                fileOutputStream.close();

                // 버퍼 데이터가 적재 완료되었다면 새로 ArrayList 를 선언하여 버퍼 데이터 초기화
                bufferString.put(partitionNo, new ArrayList<>());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    // 버퍼에 남아있는 모든 데이터를 저장하기 위한 메서드
    private void saveRemainBufferToHdfsFile() {
        bufferString.forEach((partitionNo, v) -> this.save(partitionNo));
    }

}
