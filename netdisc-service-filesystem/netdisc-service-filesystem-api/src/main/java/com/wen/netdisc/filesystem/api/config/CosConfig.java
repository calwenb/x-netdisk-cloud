package com.wen.netdisc.filesystem.api.config;

import lombok.Data;

/**
 * @author calwen
 * @since 2023/8/15
 */
@Data
public class CosConfig {
    private String secretId = "XXX";
    private String secretKey = "XXX";
    private String regionName="XXX";
    private String bucketName = "XXX";
}
