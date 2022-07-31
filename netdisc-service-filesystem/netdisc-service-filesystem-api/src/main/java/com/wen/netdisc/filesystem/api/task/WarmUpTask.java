package com.wen.netdisc.filesystem.api.task;

import com.wen.commutil.util.LogHandlerUtil;
import com.wen.netdisc.filesystem.api.servcie.EsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * WarmUp预热类
 * 1.Redis预热
 * 2.es预热
 *
 * @author Mr.文
 */
@Component
public class WarmUpTask {
    //    @Resource
//    RedisService redisService;
    @Resource
    EsService esService;
//
//    /**
//     * redis缓存 预热
//     * 每天凌晨0点执行任务
//     */
//    @Scheduled(cron = "0 0 0 */1 * ?")
//    //@Scheduled(cron = "0/15 * * * * ? ")
//    public void RedisWarmUp() {
//        RedisService service = (RedisService) new LogHandlerUtil().newProxyInstance(redisService);
//        service.redisWarmUp();
//
//    }

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
