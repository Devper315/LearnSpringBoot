package com.example.profileservice.controller;

import com.example.profileservice.dto.request.UserProfileCreateRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/user")
    public UserProfileResponse createUserProfile(@RequestBody UserProfileCreateRequest request){
        return userProfileService.createUserProfile(request);
    }

    @GetMapping("/{profileId}")
    public UserProfileResponse getUserProfileById(@PathVariable String profileId){
        return userProfileService.getUserProfile(profileId);
    }
}
