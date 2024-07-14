package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.request.RefreshRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.entity.InvalidToken;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.InvalidTokenRepo;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
	UserRepo userRepo;
	InvalidTokenRepo invalidTokenRepo;
	@NonFinal
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY;

	@NonFinal
	@Value("${jwt.validDuration}")
	protected long VALID_DURATION;

	@NonFinal
	@Value("${jwt.refreshableDuration}")
	protected long REFRESHABLE_DURATION;

	public LoginResponse authenticate(LoginRequest request) {
		User user = userRepo.findByUsername(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean result = passwordEncoder.matches(request.getPassword(), user.getPassword());
		if (!result) throw new AppException(ErrorCode.UNAUTHENTICATED);
		String token = generateToken(user);
		return LoginResponse.builder().token(token).build();
	}
	
	public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
		String token = request.getToken();
		boolean isValid = true;
		try{
			verifyToken(token, false);
		}
		catch (AppException e){
			isValid = false;
		}
		return IntrospectResponse.builder()
				.valid(isValid)
				.build();
	}
	
	private String generateToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(user.getUsername())
				.issuer("devper315")
				.issueTime(new Date())
				.expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
				.jwtID(UUID.randomUUID().toString())
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

	public void logout(LogoutRequest request) throws ParseException, JOSEException {
		SignedJWT signToken = verifyToken(request.getToken(), true);
		String jit = signToken.getJWTClaimsSet().getJWTID();
		Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
		InvalidToken invalidToken = InvalidToken.builder()
				.id(jit).expiryTime(expiryTime).build();
		invalidTokenRepo.save(invalidToken);
	}

	public LoginResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
		SignedJWT signToken = verifyToken(request.getToken(), true);
		String jit = signToken.getJWTClaimsSet().getJWTID();
		Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
		InvalidToken invalidToken = InvalidToken.builder()
									.id(jit)
									.expiryTime(expiryTime)
									.build();
		invalidTokenRepo.save(invalidToken);
		String username = signToken.getJWTClaimsSet().getSubject();
		User user = userRepo.findByUsername(username).get();
		String newToken = generateToken(user);
		return LoginResponse.builder()
				.token(newToken).build();
	}

	private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);
		JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
		Date expiryTime = isRefresh
				? new Date(claimsSet.getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.HOURS).toEpochMilli())
				: claimsSet.getExpirationTime();
		boolean verified = signedJWT.verify(verifier);
		if (invalidTokenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		if (!(verified && expiryTime.after(new Date())))
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		return signedJWT;
	}
}
