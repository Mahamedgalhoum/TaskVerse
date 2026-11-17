package com.taskverse.dto.response;

import com.taskverse.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private boolean enabled;
}
