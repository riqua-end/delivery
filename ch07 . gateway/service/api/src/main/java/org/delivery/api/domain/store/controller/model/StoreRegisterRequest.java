package org.delivery.api.domain.store.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.db.store.enums.StoreCategory;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreRegisterRequest { // 스토어 등록시 request

    @NotBlank // "", " ", null 안됨
    private String name;

    @NotBlank
    private String address;

    @NotNull // StoreCategory 는 enum 으로 매핑했으므로 문자열로 볼 수 없어서 NotNull
    private StoreCategory storeCategory;

    @NotBlank
    private String thumbnailUrl;

    @NotNull
    private BigDecimal minimumAmount;

    @NotNull
    private BigDecimal minimumDeliveryAmount;

    @NotBlank
    private String phoneNumber;
}
