package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.AuthRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.service.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AutheticationController {
	AuthService authService;
	@PostMapping("/login")
	public ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest request){
		AuthResponse result = authService.authenticate(request);
		return ApiResponse.<AuthResponse>builder().result(result).build();
	}
	
	@PostMapping("/instrospect")
	public ApiResponse<IntrospectResponse> instrospect(@RequestBody IntrospectRequest request) 
			throws JOSEException, ParseException{
		IntrospectResponse result = authService.introspect(request);
		return ApiResponse.<IntrospectResponse>builder().result(result).build();
	}

	@PostMapping("/logout")
	public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
		authService.logout(request);
		return ApiResponse.<Void>builder().build();
	}
}
