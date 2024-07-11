package com.example.profileservice.service;

import com.example.profileservice.dto.request.UserProfileCreateRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.mapper.UserProfileMapper;
import com.example.profileservice.repository.UserProfileRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepo userProfileRepo;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createUserProfile(UserProfileCreateRequest request){
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepo.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getUserProfile(String id){
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }
}
