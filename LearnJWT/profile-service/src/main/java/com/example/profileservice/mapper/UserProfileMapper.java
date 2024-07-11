package com.example.profileservice.mapper;

import com.example.profileservice.dto.request.UserProfileCreateRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile (UserProfileCreateRequest request);
    UserProfileResponse toUserProfileResponse (UserProfile entity);

}
