package com.example.apigateway.service;

import com.example.apigateway.dto.ApiResponse;
import com.example.apigateway.dto.request.AuthRequest;
import com.example.apigateway.dto.response.AuthResponse;
import com.example.apigateway.httpclient.AuthClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    AuthClient authClient;
    public Mono<ApiResponse<AuthResponse>> introspect(String token){
        AuthRequest authRequest = AuthRequest.builder()
                .token(token)
                .build();
        return authClient.introspect(authRequest);
    }
}
