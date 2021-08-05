package com.example.kafkaspringproducerwithrestcontroller.kafka;

import com.example.kafkaspringproducerwithrestcontroller.vo.UserEventVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequestMapping("")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")   // REST API 를 다른 도메인에서도 호출할 수 있도록 CORS 설정
public class ProduceController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ProduceController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/api/select")
    public void selectColor(@RequestHeader("user-agent") String userAgentName,
                            @RequestParam(value = "color") String colorName,
                            @RequestParam(value = "user") String userName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        Date now = new Date();

        UserEventVO userEventVO = new UserEventVO(simpleDateFormat.format(now), userAgentName, colorName, userName);

        ObjectMapper mapper = new ObjectMapper();
        String jsonColorLog = "";
        try {
            jsonColorLog = mapper.writeValueAsString(userEventVO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // "select-color" 토픽으로 jsonColorLog 전송
        // 메시지 키를 지정하지 않으므로 send() 메서드에는 토픽과 메시지 값만 있으면 됨
        // 추가로 데이터가 정상적으로 전송되었는지를 확인하기 위해 addCallback() 적용
        kafkaTemplate.send("select-color", jsonColorLog)
                .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                    @Override
                    public void onSuccess(SendResult<String, String> result) {
                        log.info(result.toString());
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        log.error(ex.getMessage(), ex);
                    }
                });
    }

}
