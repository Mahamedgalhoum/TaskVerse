package com.taskverse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users" , uniqueConstraints = {
        @UniqueConstraint(columnNames = "email" , name = "uk_users_email")
})
@Getter
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "user_sequence")
    private Long  id;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Column(name = "phone_number", length = 20)
    private String phone;
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true ;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "token_version", nullable = false)
    private long tokenVersion = 0;


    public User(String firstName, String lastName, String email, String password, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.enabled = true;
    }

    public void updateProfile(
            String firstName,
            String lastName,
            String phone
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
    public void disableAccount() {
        this.enabled = false;
    }
    public void enableAccount() {
        this.enabled = true;
    }

    public void incrementTokenVersion() {
        this.tokenVersion++;
    }
}
