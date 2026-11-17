package com.taskverse.service;

import com.taskverse.dto.response.AccountDeletionSettingResponse;
import com.taskverse.entity.AccountDeletionSetting;
import com.taskverse.repository.AccountDeletionSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDeletionSettingService {

    private final AccountDeletionSettingRepository accountDeletionSettingRepository;

    // Get Account Deletion Setting
    public AccountDeletionSettingResponse getSetting() {

        AccountDeletionSetting setting = getSettingEntity();

        return mapToResponse(setting);
    }

    // Update Grace Period
    public AccountDeletionSettingResponse updateGracePeriod(Integer gracePeriodDays) {

        validateGracePeriod(gracePeriodDays);

        AccountDeletionSetting setting = getSettingEntity();

        setting.setGracePeriodDays(gracePeriodDays);

        AccountDeletionSetting updatedSetting =
                accountDeletionSettingRepository.save(setting);

        return mapToResponse(updatedSetting);
    }

    // Get Grace Period Days
    public Integer getGracePeriodDays() {

        return getSettingEntity().getGracePeriodDays();
    }

    // Validate Grace Period
    private void validateGracePeriod(Integer gracePeriodDays) {

        if (gracePeriodDays == null || gracePeriodDays <= 0) {
            throw new IllegalArgumentException(
                    "Grace period days must be greater than zero"
            );
        }
    }

    // Get Setting Entity
    private AccountDeletionSetting getSettingEntity() {

        return accountDeletionSettingRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException(
                        "Account deletion settings not found"
                ));
    }

    // Map Entity to Response
    private AccountDeletionSettingResponse mapToResponse(
            AccountDeletionSetting setting) {

        return AccountDeletionSettingResponse.builder()
                .gracePeriodDays(setting.getGracePeriodDays())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }
}