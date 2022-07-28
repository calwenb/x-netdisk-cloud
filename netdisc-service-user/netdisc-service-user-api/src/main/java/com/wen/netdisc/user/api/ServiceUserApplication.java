package com.wen.netdisc.user.api;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients("com.wen.netdisc")
@ComponentScan({"com.wen.netdisc.user.api", "com.wen.netdisc.common"})
@EnableDiscoveryClient
@EnableAutoDataSourceProxy
@EnableTransactionManagement
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
        System.out.println("ServiceUserApplication " + "run~");
    }
}