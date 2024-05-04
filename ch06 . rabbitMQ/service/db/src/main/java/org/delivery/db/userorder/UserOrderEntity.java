package org.delivery.db.userorder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.userorder.enums.UserOrderStatus;
import org.delivery.db.userordermenu.UserOrderMenuEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "user_order")
public class UserOrderEntity extends BaseEntity {

    @Column(nullable = false)
    private Long userId;    // user table 1:N

    @JoinColumn(nullable = false, name = "store_id") // @ManyToOne 어노테이션은 @Column 이랑 같이 사용 불가함. @JoinColumn 으로 사용
    @ManyToOne
    private StoreEntity store; // Schema-validation: missing column [store_entity_id] in table [user_order]

    @Column(length = 50 , nullable = false , columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private UserOrderStatus status;

    @Column(precision = 11 , scale = 4 , nullable = false)
    private BigDecimal amount;

    private LocalDateTime orderedAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime cookingStartedAt;

    private LocalDateTime deliveryStartedAt;

    private LocalDateTime receivedAt;

    @OneToMany(mappedBy = "userOrder") // UserOrderEntity 와 연결
    @ToString.Exclude // toString 에 포함 시키지 않음 UserOrderEntity 의 Lombok 때문에 toString 이 무한 반복됨
    @JsonIgnore
    private List<UserOrderMenuEntity> userOrderMenuList;
}
