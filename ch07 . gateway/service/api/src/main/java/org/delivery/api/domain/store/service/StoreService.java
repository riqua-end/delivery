package org.delivery.api.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreCategory;
import org.delivery.db.store.enums.StoreStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    // 1. 유효한 스토어 가져오기
    public StoreEntity getStoreWithThrow(Long id){

        var entity = Optional.ofNullable(storeRepository.findFirstByIdAndStatusOrderByIdDesc(id, StoreStatus.REGISTERED));

        return entity.orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
    }
    // 2. 스토어 등록
    public StoreEntity register(StoreEntity storeEntity){

        return Optional.ofNullable(storeEntity)
                .map(it->{

                    it.setStar(0);
                    it.setStatus(StoreStatus.REGISTERED);
                    // TODO 등록 일시 추가하기

                    return storeRepository.save(it);
                })
                .orElseThrow(()->new ApiException(ErrorCode.NULL_POINT));
    }
    // 3. 카테고리로 스토어 검색
    public List<StoreEntity> searchByCategory(StoreCategory storeCategory){

        var list = storeRepository.findAllByStatusAndCategoryOrderByStarDesc(StoreStatus.REGISTERED,storeCategory);

        return list;
    }
    // 4. 전체 스토어
    public List<StoreEntity> registerStore(){

        var list = storeRepository.findAllByStatusOrderByIdDesc(StoreStatus.REGISTERED);

        return list;
    }
}
