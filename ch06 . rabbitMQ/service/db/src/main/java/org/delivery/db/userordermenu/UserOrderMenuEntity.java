package org.delivery.db.userordermenu;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "user_order_menu")
@EqualsAndHashCode(callSuper = true)
public class UserOrderMenuEntity extends BaseEntity {

    // userOrderEntity' is a '@ManyToOne' association and may not use '@Column' to specify column mappings (use '@JoinColumn' instead)
    @JoinColumn(nullable = false, name = "user_order_id")
    @ManyToOne
    private UserOrderEntity userOrder;   // N : 1 ( user_order table )

    @JoinColumn(nullable = false, name = "store_menu_id")
    @ManyToOne
    private StoreMenuEntity storeMenu;   // N : 1 ( store_menu table )

    @Column(length = 50 , nullable = false , columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private UserOrderMenuStatus status;
}
