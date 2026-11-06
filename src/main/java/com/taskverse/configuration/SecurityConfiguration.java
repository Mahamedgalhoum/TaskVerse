package com.taskverse.configuration;

import com.taskverse.security.CustomAuthenticationEntryPoint;
import com.taskverse.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        http.
                csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement ->
                       sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST ,
                                "/api/v1/auth/register" ,
                                "/api/v1/auth/login").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(basic ->basic.disable())
                .formLogin(formLogin -> formLogin.disable())
                 .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(Customizer.withDefaults())
                            .authenticationEntryPoint(authenticationEntryPoint)
                 );
        return http.build();

    }
    @Bean
    public AuthenticationManager authenticationManager(
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }
}
