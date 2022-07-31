package com.wen.netdisc.oauth.client.feign;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "netdisc-oauth", path = "/rpc/tokens")
public interface OauthClient extends OauthResource {

}
