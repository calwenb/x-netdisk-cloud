package com.wen.task;

import com.wen.common.utils.LogHandlerUtil;
import com.wen.servcie.EsService;
import com.wen.servcie.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * WarmUp预热类
 * 1.Redis预热
 * 2.es预热
 * @author Mr.文
 */
@Component
public class WarmUpTask {
    @Autowired
    RedisService redisService;
    @Autowired
    EsService esService;

    /**
     * redis缓存 预热
     * 每天凌晨0点执行任务
     */
    @Scheduled(cron = "0 0 0 */1 * ?")
    //@Scheduled(cron = "0/15 * * * * ? ")
    public void RedisWarmUp() {
        RedisService service = (RedisService) new LogHandlerUtil().newProxyInstance(redisService);
        service.redisWarmUp();

    }

    /**
     * es 预热
     * 每天凌晨0点执行任务
     */
    //@Scheduled(cron = "0/10 * * * * ?")
    @Scheduled(cron = "0 0 0 */1 * ?")
    public void EsWarmUp() {
        EsService service = (EsService) new LogHandlerUtil().newProxyInstance(esService);
        service.esWarmUp();
    }
}
