package org.delivery.api.domain.userorder.controller.model

import org.delivery.api.domain.store.controller.model.StoreResponse
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse

data class UserOrderDetailResponse (
    // 사용자가 주문한 건에 대한 리스폰스 정보
    var userOrderResponse: UserOrderResponse? = null,

    // 스토어에 대한 리스폰스 정보
    var storeResponse: StoreResponse? = null,

    // 어떤 메뉴를 주문했는지 N건에 대한 내역
    var storeMenuResponseList: List<StoreMenuResponse>? = null
)