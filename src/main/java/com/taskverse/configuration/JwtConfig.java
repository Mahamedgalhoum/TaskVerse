package com.taskverse.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.taskverse.security.JwtUserStatusValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;


import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    public JwtEncoder jwtEncoder(@Value("${jwt.secret}") String secret)  {

        byte[] keyBytes = Base64.getDecoder().decode(secret);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");

        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKeySpec));

    }
    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${jwt.secret}") String secret, JwtUserStatusValidator jwtUserStatusValidator) {

        byte[] keyBytes = Base64.getDecoder().decode(secret);

        SecretKeySpec secretKeySpec =
                new SecretKeySpec(keyBytes, "HmacSHA256");

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefault(),
                jwtUserStatusValidator
        ));
        return jwtDecoder;


    }
}
