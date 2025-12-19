package com.ecom.wire.security.primary;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class CorsFilterConfiguration {

    private final CorsConfiguration corsConfiguration;


    @Bean
    public FilterRegistrationBean<CorsFilter> simpleCorsFilter(){
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        FilterRegistrationBean<CorsFilter> bean=new FilterRegistrationBean<>(new CorsFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return  bean;
    }

}
