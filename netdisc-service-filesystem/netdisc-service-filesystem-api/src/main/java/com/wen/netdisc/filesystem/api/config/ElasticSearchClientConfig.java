package com.wen.netdisc.filesystem.api.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ElasticSearchClientConfig配置类
 * 配置连接信息
 *
 * @author calwen
 */
@Configuration
public class ElasticSearchClientConfig {
    @Value("${x-netdisc.elasticsearch.host}")
    private String host;

    @Value("${x-netdisc.elasticsearch.port}")
    private int port;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, "http")));
        return client;
    }
}
