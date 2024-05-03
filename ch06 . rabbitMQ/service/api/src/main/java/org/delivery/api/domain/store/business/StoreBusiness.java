package org.delivery.api.domain.store.business;

import lombok.RequiredArgsConstructor;
import org.delivery.common.annotation.Business;
import org.delivery.api.domain.store.controller.model.StoreRegisterRequest;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.db.store.enums.StoreCategory;

import java.util.List;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class StoreBusiness {

    private final StoreService storeService;

    private final StoreConverter storeConverter;

    // register
    public StoreResponse register(
            StoreRegisterRequest storeRegisterRequest
    ){
        // req -> entity -> response

        var entity = storeConverter.toEntity(storeRegisterRequest);
        var newEntity = storeService.register(entity);
        var response = storeConverter.toResponse(newEntity);

        return response;
    }

    // category 스토어 조회
    public List<StoreResponse> searchCategory(
            StoreCategory storeCategory
    ){
        // entity list -> response list

        var storeList = storeService.searchByCategory(storeCategory);

        return storeList.stream()
                .map(storeConverter::toResponse)
                .collect(Collectors.toList());
    }
}
