package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.json.JacksonObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {
        
        private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
        
        public WebMvcConfiguration(JwtTokenAdminInterceptor jwtTokenAdminInterceptor) {
                this.jwtTokenAdminInterceptor = jwtTokenAdminInterceptor;
        }
        
        /**
         * 注册自定义拦截器
         *
         * @param registry
         */
        public void addInterceptors(InterceptorRegistry registry) {
                log.info("开始注册自定义拦截器...");
                registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
        }
        
        /**
         * 通过knife4j生成接口文档
         *
         * @return
         */
        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI()
                .info(new Info()
                .title("苍穹外卖项目接口文档")
                .version("2.0")
                .description("苍穹外卖项目接口文档"));
        }
        
        /**
         * 设置静态资源映射
         *
         * @param registry
         */
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
        
        @Configuration
        public class WebConfiguration implements WebMvcConfigurer {
                
                @Override
                public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
/*                        JsonMapper jsonMapper = JsonMapper.builder()
                        .findAndAddModules()
                        .enable(SerializationFeature.INDENT_OUTPUT)
                        .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                        .build();
                        XmlMapper xmlMapper = XmlMapper.builder()
                        .findAndAddModules()
                        .defaultUseWrapper(false)
                        .build();*/
                        JacksonObjectMapper jsonMapper = new JacksonObjectMapper();
                        builder.withJsonConverter(new JacksonJsonHttpMessageConverter(jsonMapper));
//                        .withXmlConverter(new JacksonXmlHttpMessageConverter(xmlMapper));
                }
        }
        
}