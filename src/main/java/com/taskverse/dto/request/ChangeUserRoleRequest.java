package com.taskverse.dto.request;

import com.taskverse.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserRoleRequest {
    @NotNull(message = "Role is required")
    private Role role;
}
