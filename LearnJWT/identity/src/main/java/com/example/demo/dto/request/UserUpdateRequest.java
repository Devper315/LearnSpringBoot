package com.example.demo.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
	String password;
	String firstName;
	String lastName;
	LocalDate dateOfBirth;
	Set<Long> roles;
	String city;
}
