package com.wen.oauth.client.feign;

import com.wen.common.pojo.User;
import com.wen.commutil.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(value = "netdisc-oauth", path = "/token", fallback = UserFallback.class)

public interface OauthClient extends OauthResource {

}
