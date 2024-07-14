package com.example.apigateway.httpclient;

import com.example.apigateway.dto.ApiResponse;
import com.example.apigateway.dto.request.AuthRequest;
import com.example.apigateway.dto.response.AuthResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<AuthResponse>> introspect(@RequestBody AuthRequest request);
}
