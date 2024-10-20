package com.dev.identity.service.impl;

import com.dev.commons.Message;
import com.dev.commons.exception.CustomException;
import com.dev.commons.response.ErrorModel;
import com.dev.constant.Constants;
import com.dev.identity.configuration.PropertiesConfig;
import com.dev.identity.dto.request.AuthenticationRequest;
import com.dev.identity.dto.request.IntrospectRequest;
import com.dev.identity.dto.request.LogoutRequest;
import com.dev.identity.dto.request.RefreshRequest;
import com.dev.identity.dto.response.AuthenticationResponse;
import com.dev.identity.dto.response.IntrospectResponse;
import com.dev.identity.entity.InvalidatedToken;
import com.dev.identity.entity.User;
import com.dev.identity.repository.InvalidatedTokenRepository;
import com.dev.identity.repository.UserRepository;
import com.dev.identity.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PropertiesConfig propertiesConfig;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(new ErrorModel(400, Message.User.USER_DOES_NOT_EXITED)));

        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isPasswordMatch)
            throw new CustomException(new ErrorModel(401, Message.Authentication.UNAUTHENTICATED));

        var token = generateToken(user, false);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {

        log.info("Refreshing token ...");
        var signJWT = verifyToken(request.getToken(), true);
        String jit = signJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        log.info("Saving invalidated token ...");
        invalidatedTokenRepository.save(invalidatedToken);

        String username = signJWT.getJWTClaimsSet().getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(new ErrorModel(400, Message.User.USER_DOES_NOT_EXITED)));

        log.info("Creating new token ...");
        var token = generateToken(user, false);
        return AuthenticationResponse.builder()
                .id(user.getId())
                .token(token)
                .build();
    }

    @Override
    public String generateVerifyMailToken(User user) {
        return generateToken(user, true);
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {

        boolean isValid = true;
        try {
            verifyToken(request.getToken(), false);
        } catch (CustomException e) {
            log.error("Token invalid", e);
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            SignedJWT signedToken = verifyToken(request.getToken(), true);
            String jit = signedToken.getJWTClaimsSet().getJWTID();
            Date expirationTime = signedToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expirationTime)
                    .build();
            log.info("Saving invalidated token ...");
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (CustomException e) {
            log.error("Token already expired!", e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(propertiesConfig.getSecretKey().getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = isRefresh
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(propertiesConfig.getRefreshTokenTime(), ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        log.info("Start verify token ...");
        if (!(verified && expirationTime.after(new Date())))
            throw new CustomException(new ErrorModel(400, Message.Authentication.UNAUTHENTICATED));

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new CustomException(new ErrorModel(400, Message.Authentication.UNAUTHENTICATED));

        return signedJWT;
    }

    private String generateToken(User user, boolean isVerifyMail) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("admin")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(isVerifyMail
                                ? propertiesConfig.getVerifyMailTokenTime()
                                : propertiesConfig.getExpirationTime(), ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(propertiesConfig.getSecretKey().getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error when generate token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add(Constants.Authentication.AUTHORIZATION_PREFIX + role.getName());
//                stringJoiner.add(role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });
        }
        return stringJoiner.toString();
    }
}
