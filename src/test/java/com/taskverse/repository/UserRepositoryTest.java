package com.taskverse.repository;

import com.taskverse.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class UserRepositoryTest {

    private final UserRepository userRepository;

    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void shouldSaveUserSuccessfully () {
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedPassword",
                "01026368065"
        );
        User saved = userRepository.save(user);
        userRepository.flush();

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Mohamed");
        assertThat(saved.getLastName()).isEqualTo("Galhoum");
        assertThat(saved.getEmail()).isEqualTo("mohamed@gmail.com");
        assertThat(saved.getPhone()).isEqualTo("01026368065");

    }
    @Test
    void shouldFindUserByEmailSuccessfully() {

        // Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedPassword",
                "01026368065"
        );

        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("mohamed@gmail.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("mohamed@gmail.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("Mohamed");
    }

    @Test
    void shouldReturnEmptyWhenUserEmailDoesNotExist() {

        // Act
        Optional<User> foundUser = userRepository.findByEmail("unknown@gmail.com");

        // Assert
        assertThat(foundUser).isEmpty();
    }
    @Test
    void shouldReturnTrueWhenEmailExists() {

        // Arrange
        User user = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedPassword",
                "01026368065"
        );

        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByEmail("mohamed@gmail.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {

        // Act
        boolean exists = userRepository.existsByEmail("unknown@gmail.com");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void shouldNotAllowDuplicateEmail() {

        // Arrange
        User firstUser = new User(
                "Mohamed",
                "Galhoum",
                "mohamed@gmail.com",
                "encodedPassword",
                "01026368065"
        );

        User secondUser = new User(
                "Ahmed",
                "Ali",
                "mohamed@gmail.com",
                "encodedPassword",
                "01012345678"
        );

        // Act
        userRepository.save(firstUser);
        userRepository.flush();

        userRepository.save(secondUser);

        // Assert
        assertThatThrownBy(() -> userRepository.flush())
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}