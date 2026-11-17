package com.taskverse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_deletion_settings")
@Getter
@Setter
@NoArgsConstructor
public class AccountDeletionSetting {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_deletion_setting_sequence"
    )
    @SequenceGenerator(
            name = "account_deletion_setting_sequence",
            sequenceName = "account_deletion_settings_id_seq",
            allocationSize = 1
    )
    private Long id;
    @Column(name = "grace_period_days", nullable = false)
    private Integer gracePeriodDays = 30;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
