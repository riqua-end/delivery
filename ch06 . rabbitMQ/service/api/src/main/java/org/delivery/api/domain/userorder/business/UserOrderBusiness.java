package org.delivery.api.domain.userorder.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.common.annotation.Business;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.producer.UserOrderProducer;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.enums.UserOrderStatus;
import org.delivery.db.userordermenu.UserOrderMenuEntity;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
@Slf4j
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;

    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;
    private final StoreConverter storeConverter;

    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;

    private final StoreService storeService;

    private final UserOrderProducer userOrderProducer;

    // 1. 사용자 , 메뉴 id
    // 2. userOrder 생성
    // 3. userOrderMenu 생성
    // 4. 응답 생성

    public UserOrderResponse userOrder(User user , UserOrderRequest body) {

        var storeEntity = storeService.getStoreWithThrow(body.getStoreId());

        var storeMenuEntityList = body.getStoreMenuIdList()
                .stream()
                .map(storeMenuService::getStoreMenuWithThrow)
                .collect(Collectors.toList());

        var userOrderEntity = userOrderConverter.toEntity(user, storeEntity, storeMenuEntityList);

        // 주문
        var newUserOrderEntity = userOrderService.order(userOrderEntity);

        // 맵핑
        var userOrderMenuEntityList = storeMenuEntityList.stream()
                .map(it -> {
                    // menu + user order

                    return userOrderMenuConverter.toEntity(newUserOrderEntity, it);
                })
                .collect(Collectors.toList());

        // 주문내역 기록 남기기
        userOrderMenuEntityList.forEach(userOrderMenuService::order);

        // 비동기로 가맹점에 주문 알리기
        userOrderProducer.sendOrder(newUserOrderEntity);

        // response

        return userOrderConverter.toResponse(newUserOrderEntity);
    }

    public List<UserOrderDetailResponse> current(User user) {

        // 사용자가 주문한 건 불러오기
        var userOrderEntityList = userOrderService.current(user.getId());

        // 주문 1건 씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream().map(userOrderEntity -> {

            // 사용자가 주문한 메뉴
            //var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
            var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList().stream()
                    .filter(it->it.getStatus().equals(UserOrderMenuStatus.REGISTERED))
                    .collect(Collectors.toList())
                    ;

            // 사용자가 주문한 메뉴에 대한 스토어메뉴 리스트
            var storeMenuEntityList = userOrderMenuEntityList.stream()
                    .map(userOrderMenuEntity -> {
                        return userOrderMenuEntity.getStoreMenu();
                    }).collect(Collectors.toList());

            // 사용자가 주문한 스토어
            //var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStore().getId());
            var storeEntity = userOrderEntity.getStore(); // 연관관계를 통해서 바로 접근

            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(userOrderEntity)) // 사용자의 주문
                    .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList)) // 어떤 메뉴를 담았는지
                    .storeResponse(storeConverter.toResponse(storeEntity)) // 어떤 스토어의 상품인지
                    .build();
        }).collect(Collectors.toList());

        return userOrderDetailResponseList;
    }

    public List<UserOrderDetailResponse> history(User user) {

        // 사용자가 주문한 건 불러오기
        var userOrderEntityList = userOrderService.history(user.getId());

        // 주문 1건 씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream().map(userOrderEntity -> {

            // 사용자가 주문한 메뉴
            //var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
            var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList().stream()
                    .filter(it->it.getStatus().equals(UserOrderMenuStatus.REGISTERED))
                    .collect(Collectors.toList())
                    ;

            // 사용자가 주문한 메뉴에 대한 스토어메뉴 리스트
            //return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenu().getId());
            var storeMenuEntityList = userOrderMenuEntityList.stream()
                    .map(UserOrderMenuEntity::getStoreMenu)
                    .collect(Collectors.toList())
                    ;

            // 사용자가 주문한 스토어
            //var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStore().getId());
            var storeEntity = userOrderEntity.getStore();

            return UserOrderDetailResponse.builder()
                    .userOrderResponse(userOrderConverter.toResponse(userOrderEntity)) // 사용자의 주문
                    .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList)) // 어떤 메뉴를 담았는지
                    .storeResponse(storeConverter.toResponse(storeEntity)) // 어떤 스토어의 상품인지
                    .build();
        }).collect(Collectors.toList());

        return userOrderDetailResponseList;
    }

    public UserOrderDetailResponse read(User user, Long orderId) {

        var userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());

        // 사용자가 주문한 메뉴
        //var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());
        var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList().stream()
                .filter(it->it.getStatus().equals(UserOrderMenuStatus.REGISTERED))
                .collect(Collectors.toList())
                ;
        // 사용자가 주문한 메뉴에 대한 스토어메뉴 리스트
        //var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenu().getId());
        //return storeMenuEntity;
        var storeMenuEntityList = userOrderMenuEntityList.stream()
                .map(UserOrderMenuEntity::getStoreMenu)
                .collect(Collectors.toList());

        // 사용자가 주문한 스토어
        //var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStore().getId());
        var storeEntity = userOrderEntity.getStore();

        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity)) // 사용자의 주문
                .storeMenuResponseList(storeMenuConverter.toResponse(storeMenuEntityList)) // 어떤 메뉴를 담았는지
                .storeResponse(storeConverter.toResponse(storeEntity)) // 어떤 스토어의 상품인지
                .build();
    }
}
