package org.delivery.db.store;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.store.enums.StoreCategory;
import org.delivery.db.store.enums.StoreStatus;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "store")
public class StoreEntity extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 150, nullable = false)
    private String address;

    @Column(length = 50, nullable = false,columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Column(length = 50, nullable = false, columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    private double star; // 평점

    @Column(length = 200, nullable = false)
    private String thumbnailUrl;

    @Column(precision = 11,scale = 4, nullable = false)
    private BigDecimal minimumAmount; // 최소 주문 금액

    @Column(precision = 11,scale = 4, nullable = false)
    private BigDecimal minimumDeliveryAmount; // 최소 배달비

    @Column(length = 20)
    private String phoneNumber;
}
