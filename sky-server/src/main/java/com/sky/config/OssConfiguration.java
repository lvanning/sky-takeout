package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
        
        @Bean
        public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
                log.info("创建并装配阿里云OSS工具类实例");
                return new AliOssUtil(aliOssProperties);
        }
        
        @Bean
        @ConfigurationProperties(prefix = "sky.alioss")
        public AliOssProperties aliOssProperties(){
                return new AliOssProperties();
        }
}
