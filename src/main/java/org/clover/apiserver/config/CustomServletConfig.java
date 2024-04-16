package org.clover.apiserver.config;

import lombok.extern.log4j.Log4j2;
import org.clover.apiserver.controller.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Log4j2
public class CustomServletConfig implements WebMvcConfigurer {

    //formatter를 어노테이션 없이 만든 대신 설정을 따로 해주어야함
    @Override
    public void addFormatters(FormatterRegistry registry) {

        log.info("-------------------------------");
        log.info("addFormatters");
        registry.addFormatter(new LocalDateFormatter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .maxAge(500)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedOrigins("*");
    }
}