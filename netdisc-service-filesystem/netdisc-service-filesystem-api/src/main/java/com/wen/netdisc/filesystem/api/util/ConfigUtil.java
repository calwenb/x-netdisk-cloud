package com.wen.netdisc.filesystem.api.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ConfigUtil {
    @Value("${x-netdisc.elasticsearch.index}")
    private String esIndex;
}
