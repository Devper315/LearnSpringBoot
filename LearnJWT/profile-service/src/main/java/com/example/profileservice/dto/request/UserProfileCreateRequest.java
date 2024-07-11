package com.example.profileservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileCreateRequest {
    Long userId;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
}
