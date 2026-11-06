    package com.taskverse.service;

    import com.taskverse.entity.User;
    import com.taskverse.repository.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
    import org.springframework.security.oauth2.jwt.JwsHeader;
    import org.springframework.security.oauth2.jwt.JwtClaimsSet;
    import org.springframework.security.oauth2.jwt.JwtEncoder;
    import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
    import org.springframework.stereotype.Service;

    import java.time.Instant;

    @Service
    @RequiredArgsConstructor
    public class AuthService {
        private final UserRepository userRepository;
        private final AuthenticationManager authenticationManager;
        private final JwtEncoder jwtEncoder;
        @Value("${jwt.expiration}")
        private long jwtExpiration;

        public Authentication  authenticate(String email, String password) {

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(email, password);

            return authenticationManager.authenticate(authentication);
        }

        public String login(String email, String password) {

            Authentication authentication = authenticate(email, password);

            return generateToken(authentication);
        }

        public long getExpirationInSeconds() {
            return jwtExpiration / 1000;
        }

        private JwtClaimsSet buildClaims(Authentication authentication) {

            Instant now = Instant.now();
            User user =userRepository.findByEmail(authentication.getName())
                    .orElseThrow(()-> new UsernameNotFoundException("User not found"));

            return JwtClaimsSet.builder()
                    .subject(authentication.getName())
                    .claim("tokenVersion", user.getTokenVersion())
                    .issuedAt(now)
                    .expiresAt(now.plusMillis(jwtExpiration))
                    .build();
        }

        private String generateToken(Authentication authentication) {

            JwtClaimsSet claims = buildClaims(authentication);

            JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

            return jwtEncoder.encode(
                    JwtEncoderParameters.from(header, claims)
            ).getTokenValue();
        }

    }
