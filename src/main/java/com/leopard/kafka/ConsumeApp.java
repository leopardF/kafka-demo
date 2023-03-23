package com.leopard.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author leopard
 */
@EnableEurekaClient
@SpringBootApplication
@EnableScheduling
public class ConsumeApp {

    public static void main(String[] args) {
        SpringApplication.run(ConsumeApp.class, args);
    }
}
