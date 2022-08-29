package com.wen.netdisc.filesystem.api.task;

import com.wen.commutil.util.LogHandlerUtil;
import com.wen.netdisc.filesystem.api.servcie.EsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * WarmUp预热类
 * 1.ES预热
 *
 * @author calwen
 */
@Component
public class WarmUpTask {

    @Resource
    EsService esService;

    /**
     * es 预热
     * 每天凌晨0点执行任务
     */
    @Scheduled(cron = "0/10 * * * * ?")
//    @Scheduled(cron = "0 0 0 */1 * ?")
    public void EsWarmUp() {
        EsService service = (EsService) new LogHandlerUtil().newProxyInstance(esService);
        service.esWarmUp();
    }

}
