package com.lzy.seata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.lzy.seata.mapper")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TransactionRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionRecordApplication.class, args);
    }

}
