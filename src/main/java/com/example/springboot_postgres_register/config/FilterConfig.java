package com.example.springboot_postgres_register.config;

import com.example.springboot_postgres_register.filter.JwtHeaderAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtHeaderAuthFilter> jwtCookieFilter() {
        FilterRegistrationBean<JwtHeaderAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtHeaderAuthFilter());
        registrationBean.addUrlPatterns("/v1/api/users/*"); // apply to all secured routes
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
