package com.stilyanov.apigateway.config;

import com.stilyanov.apigateway.UserFeignClientInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

@Configuration
public class UserFeignClientInterceptorConfiguration {

    @Bean
    public RequestInterceptor getUserFeignClientInterceptor(OAuth2AuthorizedClientService clientService) {
        return new UserFeignClientInterceptor(clientService);
    }
}
