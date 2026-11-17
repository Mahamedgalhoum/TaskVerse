package com.taskverse.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountDeletionSettingRequest {
    @NotNull(message = "Grace period days is required")
    @Positive(message = "Grace period days must be greater than zero")
    private Integer gracePeriodDays;
}
