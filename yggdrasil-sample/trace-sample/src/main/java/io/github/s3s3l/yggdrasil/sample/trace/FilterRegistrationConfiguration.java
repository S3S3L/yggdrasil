package io.github.s3s3l.yggdrasil.sample.trace;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;

@Configuration
public class FilterRegistrationConfiguration {
    @Bean
    FilterRegistrationBean<Filter> endUserFixFilterRegistration(CrosFilter crosFilter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(crosFilter);
        registration.addUrlPatterns("/*");
        registration.setName("crosFilter");
        registration.setOrder(1);
        return registration;
    }
}
