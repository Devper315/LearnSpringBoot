package com.example.demo.controller;


import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
@Slf4j
public class UserController {
	UserService userService;
	
	@PostMapping("/register")
	public ApiResponse<UserResponse> createUser (@RequestBody @Valid UserCreationRequest request) {
		ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder().build();
		apiResponse.setResult(userService.createUserFromRequest(request));
		return apiResponse;
	}
	
	@GetMapping
	public ApiResponse<List<User>> getUsers(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.info("Username: {}", auth.getName());
		auth.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
		return ApiResponse.<List<User>>builder()
				.result(userService.getUsers())
				.build();
	}
	
	@GetMapping("/{userId}")
	public ApiResponse<UserResponse> getUserById(@PathVariable Long userId) {
		return ApiResponse.<UserResponse>builder()
				.result(userService.getUserById(userId))
				.build();
	}
	
	@GetMapping("/my-info")
	public ApiResponse<UserResponse> getMyInfo(){
		return ApiResponse.<UserResponse>builder()
						  .result(userService.getMyInfo())
						  .build();
	}
	
	@PutMapping("/{userId}")
	public UserResponse updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
		return userService.updateUser(userId, request);
	}
	
	@DeleteMapping("/{userId}")
	public String deleteMember(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return "Member has been deleted";
	}
	
}
