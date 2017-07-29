package com.example.recruitment.task.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Lukasz S. on 04.04.2017.
 */
@Configuration
@EnableWebMvc
@EnableAsync
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/repositories/**")
                .allowedMethods("GET")
                .allowedHeaders("GET")
                .allowCredentials(false).maxAge(3600);
    }


}