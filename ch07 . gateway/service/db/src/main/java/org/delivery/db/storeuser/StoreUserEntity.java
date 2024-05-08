package org.delivery.db.storeuser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.storeuser.enums.StoreUserRole;
import org.delivery.db.storeuser.enums.StoreUserStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "store_user")
@Entity
@SuperBuilder
public class StoreUserEntity extends BaseEntity {

    @Column(nullable = false)
    private Long storeId;

    @Column(length = 100 , nullable = false)
    private String email;

    @Column(length = 100 , nullable = false)
    private String password;

    @Column(length = 50 , nullable = false, columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private StoreUserStatus status;

    @Column(length = 50 , nullable = false, columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private StoreUserRole role;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    private LocalDateTime lastLoginAt;
}
