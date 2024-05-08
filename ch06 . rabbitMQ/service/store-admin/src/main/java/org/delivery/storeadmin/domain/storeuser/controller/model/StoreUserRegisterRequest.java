package org.delivery.storeadmin.domain.storeuser.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.db.storeuser.enums.StoreUserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreUserRegisterRequest {

    @NotNull
    @JsonProperty("store_name")
    private String storeName;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private StoreUserRole role;
}
