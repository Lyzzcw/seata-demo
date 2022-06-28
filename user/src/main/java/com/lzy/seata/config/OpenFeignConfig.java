package com.lzy.seata.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2021/12/16
 * Time: 16:41
 * Description: 调整日志等级
 */
@Configuration
public class OpenFeignConfig {
    @Bean
    Logger.Level feignLoggerLevel(){
        return  Logger.Level.FULL;
    }
}
