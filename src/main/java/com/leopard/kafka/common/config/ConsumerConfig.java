package com.leopard.kafka.common.config;//package com.bottle.kafka.common.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.stereotype.Component;
//
///**
// * 服务消费
// */
//@Slf4j
//@Component
//public class ConsumerConfig {
//
//
//    /**
//     * 自定义 ConcurrentKafkaListenerContainerFactory 初始化消费者
//     *
//     * @return ConcurrentKafkaListenerContainerFactory
//     */
//    @Bean("ackContainerFactory")
//    public ConcurrentKafkaListenerContainerFactory ackContainerFactory(ConsumerFactory<String, String> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
//        factory.setConsumerFactory(consumerFactory);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }
//
//}