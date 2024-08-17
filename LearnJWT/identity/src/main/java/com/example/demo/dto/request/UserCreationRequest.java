package com.example.demo.dto.request;

import com.example.demo.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
	String username;
	@Size(min = 8, message = "INVALID_PASSWORD")
	String password;
	String firstName;
	String lastName;
	@DobConstraint(min = 18)
	LocalDate dateOfBirth;
	List<Long> roles;
	String city;
}
