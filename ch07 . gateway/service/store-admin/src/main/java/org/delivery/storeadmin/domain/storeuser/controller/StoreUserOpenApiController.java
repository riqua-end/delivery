package org.delivery.storeadmin.domain.storeuser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.delivery.storeadmin.domain.storeuser.business.StoreUserBusiness;
import org.delivery.storeadmin.domain.storeuser.controller.model.StoreUserRegisterRequest;
import org.delivery.storeadmin.domain.storeuser.controller.model.StoreUserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-api/store-user")
@RequiredArgsConstructor
public class StoreUserOpenApiController {

    private final StoreUserBusiness storeUserBusiness;

    @PostMapping("")
    public StoreUserResponse register(
            @Valid
            @RequestBody StoreUserRegisterRequest storeUserRegisterRequest
    ) {
        var response = storeUserBusiness.register(storeUserRegisterRequest);

        return response;
    }
}
