package com.taskverse.repository;

import com.taskverse.entity.User;
import com.taskverse.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    long countByRole(Role role);
    long countByRoleAndEnabledTrue(Role role);
}
