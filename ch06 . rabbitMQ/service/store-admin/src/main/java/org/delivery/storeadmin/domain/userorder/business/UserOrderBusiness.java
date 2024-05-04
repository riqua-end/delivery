package org.delivery.storeadmin.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.common.message.model.UserOrderMessage;
import org.delivery.storeadmin.domain.sse.connection.SseConnectionPool;
import org.delivery.storeadmin.domain.sse.connection.model.UserSseConnection;
import org.delivery.storeadmin.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.storeadmin.domain.storemenu.service.StoreMenuService;
import org.delivery.storeadmin.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.storeadmin.domain.userorder.converter.UserOrderConverter;
import org.delivery.storeadmin.domain.userorder.service.UserOrderService;
import org.delivery.storeadmin.domain.userordermenu.service.UserOrderMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;

    private final SseConnectionPool sseConnectionPool;

    private final UserOrderMenuService userOrderMenuService;

    private final StoreMenuService storeMenuService;

    private final StoreMenuConverter storeMenuConverter;

    /**
     * 주문
     * 주문 내역 찾기
     * 스토어 찾기
     * 연결된 세션 찾아서
     * push
     */

    public void pushUserOrder(UserOrderMessage userOrderMessage){
        var userOrderEntity = userOrderService.getUserOrder(userOrderMessage.getUserOrderId())
                .orElseThrow(()->new RuntimeException("사용자 주문 내역 없음"));

        // user order menu
        var userOrderMenuList = userOrderMenuService.getUserOrderMenuList(userOrderEntity.getId());

        // user order menu -> store menu
        var storeMenuResponseList = userOrderMenuList.stream()
                .map(userOrderMenuEntity-> {
                    return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenu().getId());
                })
                .map(storeMenuEntity -> {
                    return storeMenuConverter.toResponse(storeMenuEntity);
                })
                .collect(Collectors.toList());

        var userOrderResponse = userOrderConverter.toResponse(userOrderEntity);

        // response
        var push = UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderResponse)
                .storeMenuResponseList(storeMenuResponseList)
                .build();

        log.info("Retrieved storeId: {}", userOrderEntity.getStore());
        var userConnection = sseConnectionPool.getSession(userOrderEntity.getStore().getId().toString());
        log.info("Retrieved userConnection : {}", userConnection); // 여기서 userConnection 값 출력

        // 사용자에게 push
        /*if(userConnection != null){
            userConnection.sendMessage(push);
        }*/
    }
}
