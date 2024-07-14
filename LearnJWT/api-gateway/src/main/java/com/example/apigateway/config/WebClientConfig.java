package com.example.apigateway.config;

import com.example.apigateway.httpclient.AuthClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8080/api/v1/identity")
                .build();
    }

    @Bean
    public AuthClient authClient(WebClient webClient){
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return proxyFactory.createClient(AuthClient.class);
    }

}
