package com.taskverse.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccountDeletionSettingResponse {
    private Integer gracePeriodDays;
    private LocalDateTime updatedAt;
}
