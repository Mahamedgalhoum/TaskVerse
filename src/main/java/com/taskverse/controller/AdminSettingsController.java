package com.taskverse.controller;

import com.taskverse.dto.request.UpdateAccountDeletionSettingRequest;
import com.taskverse.dto.response.AccountDeletionSettingResponse;
import com.taskverse.dto.response.ApiResponse;
import com.taskverse.service.AccountDeletionSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/settings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSettingsController {
    private final AccountDeletionSettingService accountDeletionSettingService;

    @GetMapping("/account-deletion")
    public ResponseEntity<ApiResponse<AccountDeletionSettingResponse>> getAccountDeletionSetting() {

        AccountDeletionSettingResponse setting = accountDeletionSettingService.getSetting();

        ApiResponse<AccountDeletionSettingResponse> response =
                ApiResponse.success("Account Deletion Setting Retrieved Successfully", setting);

        return ResponseEntity.ok(response);


    }

    @PatchMapping("/account-deletion")
    public ResponseEntity<ApiResponse<AccountDeletionSettingResponse>> updateAccountDeletionSetting(
            @Valid @RequestBody UpdateAccountDeletionSettingRequest request) {

        AccountDeletionSettingResponse setting = accountDeletionSettingService
                .updateGracePeriod(request.getGracePeriodDays());

        ApiResponse<AccountDeletionSettingResponse> response =
                ApiResponse.success("Account Deletion Setting Updated Successfully", setting);

        return ResponseEntity.ok(response);

    }
}

