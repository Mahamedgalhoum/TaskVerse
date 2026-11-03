package com.taskverse.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(PasswordConfig.class)
public class PasswordConfigTest {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    PasswordConfigTest(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Test
    void shouldEncodePasswordSuccessfully() {
        String rawPassword = "TaskVerse@123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertThat(encodedPassword).isNotNull().isNotEqualTo(rawPassword);

        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        assertThat(matches).isTrue();
    }
    @Test
    void shouldReturnFalseWhenPasswordIsIncorrect() {
        String rawPassword = "TaskVerse@123";
        String wrongPassword = "WrongPassword@123";
        String encoded = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(wrongPassword, encoded);
        assertThat(matches).isFalse();
    }
    @Test
    void shouldGenerateDifferentHashesForSamePassword() {
        String rawPassword = "TaskVerse@123";
        String firstHash = passwordEncoder.encode(rawPassword);

        String secondHash = passwordEncoder.encode(rawPassword);
        assertThat(firstHash).isNotEqualTo(secondHash);
    }
}
