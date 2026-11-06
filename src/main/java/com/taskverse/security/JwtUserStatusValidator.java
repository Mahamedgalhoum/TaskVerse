package com.taskverse.security;

import com.taskverse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUserStatusValidator implements OAuth2TokenValidator<Jwt> {

    private final UserRepository userRepository;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {

        Number tokenVersion = jwt.getClaim("tokenVersion");

        boolean validUser = userRepository.findByEmail(jwt.getSubject())
                .filter(user -> user.isEnabled())
                .filter(user -> tokenVersion != null &&
                        user.getTokenVersion() == tokenVersion.longValue())
                .isPresent();

        if (!validUser) {
            OAuth2Error error = new OAuth2Error(
                    "invalid_token",
                    "Invalid token",
                    null
            );

            return OAuth2TokenValidatorResult.failure(error);
        }

        return OAuth2TokenValidatorResult.success();
    }
}