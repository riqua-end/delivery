package org.delivery.db.userorder

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.delivery.db.store.StoreEntity
import org.delivery.db.userorder.enums.UserOrderStatus
import org.delivery.db.userordermenu.UserOrderMenuEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "user_order")
class UserOrderEntity( // kotlin 에서 Entity 는 data class 로는 적합하지 않음 , toString 이나 equals 등등 문제 발생 소지가 있음

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? =null,

    @field:Column(nullable = false)
    var userId: Long?=null,

    @field:JoinColumn(nullable = false, name = "store_id") // @ManyToOne 어노테이션은 @Column 이랑 같이 사용 불가함. @JoinColumn 으로 사용
    // @ManyToOne 어노테이션은 @Column 이랑 같이 사용 불가함. @JoinColumn 으로 사용
    @field:ManyToOne
    var store: StoreEntity?=null,

    @field:Column(length = 50, nullable = false, columnDefinition = "varchar")
    @field:Enumerated(EnumType.STRING)
    var status: UserOrderStatus?=null,

    @field:Column(precision = 11, scale = 4, nullable = false)
    var amount: BigDecimal?=null,

    var orderedAt: LocalDateTime?=null,

    var acceptedAt: LocalDateTime?=null,

    var cookingStartedAt: LocalDateTime?=null,

    var deliveryStartedAt: LocalDateTime?=null,

    var receivedAt: LocalDateTime?=null,

    @field:OneToMany(mappedBy = "userOrder") // UserOrderEntity 와 연결
    @field:JsonIgnore
    var userOrderMenuList: MutableList<UserOrderMenuEntity>?=null,
) {
    override fun toString(): String {
        return "UserOrderEntity(id=$id, userId=$userId, store=$store, status=$status, amount=$amount, orderedAt=$orderedAt, acceptedAt=$acceptedAt, cookingStartedAt=$cookingStartedAt, deliveryStartedAt=$deliveryStartedAt, receivedAt=$receivedAt)"
    }
}