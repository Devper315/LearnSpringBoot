package com.example.demo.service;

import com.example.demo.dto.request.AuthRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
	UserRepo userRepo;
	@NonFinal
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY;

	public AuthResponse authenticate(AuthRequest request) {
		User user = userRepo.findByUsername(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean result = passwordEncoder.matches(request.getPassword(), user.getPassword());
		if (!result) throw new AppException(ErrorCode.UNAUTHENTICATED);
		String token = generateToken(user);
		return AuthResponse.builder().token(token).build();
	}
	
	public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
		String token = request.getToken();
		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);
		Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
		boolean verified = signedJWT.verify(verifier);
		return IntrospectResponse.builder()
				.valid(verified && expiryTime.after(new Date()))
				.build();
	}
	
	private String generateToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(user.getUsername())
				.issuer("devper315")
				.issueTime(new Date())
				.expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
				.claim("scope", buildScope(user))
				.build();
		Payload payload = new Payload(jwtClaimsSet.toJSONObject());
		JWSObject jwsObject = new JWSObject(header, payload);
		try {
			jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			e.printStackTrace();
			return "Không thể tạo token";
		}
	}
	
	private String buildScope(User user) {
		StringJoiner joiner = new StringJoiner(" ");
		if (!CollectionUtils.isEmpty(user.getRoles())) {
			user.getRoles().forEach(role -> {
				joiner.add("ROLE_" + role.getName());
//				if (!role.getPermissionSet().isEmpty())
//					role.getPermissionSet().forEach(permission -> joiner.add(permission.getName()));
			});
		}
		return joiner.toString();
	}
}
