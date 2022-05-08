package com.wen;


import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * netdisc主启动类
 * 入口
 *
 * @author Mr.文
 */
@EnableAsync
@EnableAutoDataSourceProxy
@EnableFeignClients
@EnableCaching
@EnableTransactionManagement
@EnableScheduling
@EnableAspectJAutoProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceFilesystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFilesystemApplication.class, args);
        System.out.println("ServiceFilesystemApplication" + " run~");
    }

}
