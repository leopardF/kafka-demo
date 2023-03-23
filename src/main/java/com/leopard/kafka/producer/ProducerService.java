package com.leopard.kafka.producer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;


/**
 * kafka消息生产者
 *
 * @author leopard.ji
 */
@Component
@Slf4j
public class ProducerService {

    @Value("${spring.kafka.declare-topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    /**
     * 发送KAFKA
     * (注：主题topic有多个，可作为参数传入)
     *
     * @param topic   发送主题队列
     * @param message string消息体，如果是实体类转JSONString
     */
    public void send(String topic, String message) {
        log.info("topic=" + topic + ",message=" + message);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

        future.addCallback(success -> log.info("KafkaMessageProducer 发送消息成功！"),
                fail -> log.error("KafkaMessageProducer 发送消息失败！"));
    }

}
