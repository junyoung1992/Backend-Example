package com.pipeline;

import com.google.gson.Gson;
import com.pipeline.config.ElasticSearchSinkConnectorConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * ElasticSearchSinkTask 클래스에는 실질적인 엘라스틱서치 적재 로직이 들어간다.
 */

@Slf4j
public class ElasticSearchSinkTask extends SinkTask {

    private ElasticSearchSinkConnectorConfig config;
    private RestHighLevelClient esClient;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        try {
            config = new ElasticSearchSinkConnectorConfig(props);
        } catch (ConfigException e) {
            throw new ConnectException(e.getMessage(), e);
        }

        // 엘라스틱서치에 적재하기 위해 RestHighLevelClient 인스턴스를 생성한다.
        // 입력한 호스트와 포트를 기반으로 RestHighLevelClient 인스턴스를 생성한다.
        esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(
                        config.getString(config.ES_CLUSTER_HOST),
                        config.getInt(config.ES_CLUSTER_PORT))));
    }

    @Override
    public void put(Collection<SinkRecord> records) {
        // 레코드가 1개 이상 들어올 경우, 엘라스틱서치로 전송하기 위한 BulkRequest 인스턴스를 생성한다.
        // BulkRequest 는 1개 이상의 데이터들을 묶음으로 엘라스틱서치로 전송할 때 사용된다.
        // 레코드들은 BulkRequest 인스턴스에 추가된다.
        // BulkRequest 에 데이터를 추가할 때는 Map 타입의 데이터와 인덱스 이름이 필요하다.
        // 토픽의 메시지 값은 JSON 형태의 String 타입이므로 JSON 을 Map 으로 변환하여 사용한다.
        // 인덱스는 사용자로부터 받은 값을 기반으로 설정한다.
        if(records.size() > 0) {
            BulkRequest bulkRequest = new BulkRequest();

            for (SinkRecord record: records) {
                Gson gson = new Gson();
                Map map = gson.fromJson(record.value().toString(), Map.class);
                bulkRequest.add(new IndexRequest(config.getString(config.ES_INDEX))
                        .source(map, XContentType.JSON));

                log.info("record : {}", record.value());
            }

            // bulkRequest 에 담은 데이터들을 bulkAsync() 메서드로 전송한다.
            // bulkAsync() 메서드를 사용하면 데이터를 전송하고 난 뒤, 결과를 비동기로 받아서 확인할 수 있다.
            esClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkResponse) {
                    if (bulkResponse.hasFailures()) {
                        log.error(bulkResponse.buildFailureMessage());
                    } else {
                        log.info("bulk save success");
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    @Override
    // flush() 메서드는 일정 주기마다 호출된다.
    // 여기서는 put() 메서드에서 레코드들을 받아서 엘라스틱서치에 데이터를 전송하므로
    // flush() 메서드에서는 추가로 작성해야 할 코드는 없다.
    public void flush(Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
        log.info("flush");
    }

    @Override
    // 커넥터가 종료될 경우 엘라스틱서치와 연동하는 esClient 변수를 안전하게 종료한다.
    public void stop() {
        try {
            esClient.close();
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

}
