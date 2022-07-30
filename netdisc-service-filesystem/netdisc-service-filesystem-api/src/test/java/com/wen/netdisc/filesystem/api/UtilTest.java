package com.wen.netdisc.filesystem.api;

import com.wen.netdisc.filesystem.api.util.ConfigUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UtilTest {
    @Resource
    ConfigUtil configUtil;

    @Test
    void t1() {
        System.out.println(configUtil.getEsIndex());


    }
}
