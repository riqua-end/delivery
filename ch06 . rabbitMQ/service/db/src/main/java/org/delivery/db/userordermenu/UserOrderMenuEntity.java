package org.delivery.db.userordermenu;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "user_order_menu")
@EqualsAndHashCode(callSuper = true)
public class UserOrderMenuEntity extends BaseEntity {

    @Column(nullable = false)
    private Long userOrderId;   // 1 : N ( user_order table )

    @Column(nullable = false)
    private Long storeMenuId;   // 1 : N ( store_menu table )

    @Column(length = 50 , nullable = false , columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private UserOrderMenuStatus status;
}
