package org.delivery.api.domain.storemenu.converter;

import org.delivery.api.common.annotation.Converter;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.db.storemenu.StoreMenuEntity;

import java.util.Optional;

@Converter
public class StoreMenuConverter {

    public StoreMenuEntity toEntity(StoreMenuRegisterRequest request){

        return Optional.ofNullable(request)
                .map(it -> {

                    return StoreMenuEntity.builder()
                            .storeId(request.getStoreId())
                            .name(request.getName())
                            .amount(request.getAmount())
                            .thumbnailUrl(request.getThumbnailUrl())
                            .build()
                            ;
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
    }


    public StoreMenuResponse toResponse(
        StoreMenuEntity storeMenuEntity
    ){

        return Optional.ofNullable(storeMenuEntity)
                .map(it -> {

                    return StoreMenuResponse.builder()
                            .id(storeMenuEntity.getId())
                            .storeId(storeMenuEntity.getStoreId())
                            .name(storeMenuEntity.getName())
                            .amount(storeMenuEntity.getAmount())
                            .status(storeMenuEntity.getStatus())
                            .thumbnailUrl(storeMenuEntity.getThumbnailUrl())
                            .likeCount(storeMenuEntity.getLikeCount())
                            .sequence(storeMenuEntity.getSequence())
                            .build()
                            ;
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
    }
}
