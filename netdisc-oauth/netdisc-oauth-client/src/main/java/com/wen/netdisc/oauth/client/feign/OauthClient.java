package com.wen.netdisc.oauth.client.feign;

//@FeignClient(value = "netdisc-oauth", path = "/token", fallback = UserFallback.class)

public interface OauthClient extends OauthResource {

}
