package com.example.demo.config;

import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomJWTDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    String signerKey;

    @Autowired
    AuthService authService;

    NimbusJwtDecoder nimbusJwtDecoder = null;
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            boolean valid = authService.introspect(IntrospectRequest.builder().token(token).build()).isValid();
            if (!valid) throw new AppException(ErrorCode.UNAUTHENTICATED);
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

        if (nimbusJwtDecoder == null){
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        System.out.println(nimbusJwtDecoder);
        return nimbusJwtDecoder.decode(token);
    }
}
