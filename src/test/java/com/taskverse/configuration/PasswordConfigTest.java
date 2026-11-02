//package com.taskverse.configuration;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class PasswordConfigTest {
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Test
//     void testPasswordEncoding() {
//
//        String rawPassword = "12345" ;
//        String encodedPassword =  passwordEncoder.encode(rawPassword);
//        System.out.println(encodedPassword);
//        assertNotEquals(rawPassword, encodedPassword);
//        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
//        assertFalse(passwordEncoder.matches("Wrong123", encodedPassword));
//    }
//}
