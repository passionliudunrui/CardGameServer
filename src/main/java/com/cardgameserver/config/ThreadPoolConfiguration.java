package com.cardgameserver.config;

import com.dtp.core.support.ThreadPoolCreator;
import com.dtp.core.thread.DtpExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolConfiguration {
    @Bean
    public DtpExecutor dtpExecutor(){

        return ThreadPoolCreator.createDynamicFast("dtpExecutor1");
    }

}
