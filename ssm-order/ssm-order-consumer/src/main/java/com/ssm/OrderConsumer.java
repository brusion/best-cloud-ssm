package com.ssm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author brusion
 * @date 2018/9/4
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderConsumer {

    public static void main(String[] args) {
        SpringApplication.run(OrderConsumer.class,args);
    }
}
