package com.taskverse.repository;

import com.taskverse.entity.AccountDeletionSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDeletionSettingRepository extends JpaRepository<AccountDeletionSetting, Long> {

    Optional<AccountDeletionSetting> findFirstByOrderByIdAsc();


}
