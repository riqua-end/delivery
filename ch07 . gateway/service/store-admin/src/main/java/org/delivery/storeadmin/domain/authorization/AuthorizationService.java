package org.delivery.storeadmin.domain.authorization;

import lombok.RequiredArgsConstructor;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreStatus;
import org.delivery.storeadmin.domain.authorization.model.UserSession;
import org.delivery.storeadmin.domain.storeuser.service.StoreUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final StoreUserService storeUserService;

    private final StoreRepository storeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var storeUserEntity =  storeUserService.getRegisterUser(username);

        var storeEntity = Optional.ofNullable(
            storeRepository.findFirstByIdAndStatusOrderByIdDesc(
                storeUserEntity.get().getStoreId(),
                StoreStatus.REGISTERED
            )
        );

        return storeUserEntity.map(it->{

            var userSession = UserSession.builder()
                    .userId(it.getId())
                    .email(it.getEmail())
                    .password(it.getPassword())
                    .status(it.getStatus())
                    .role(it.getRole())
                    .registeredAt(it.getRegisteredAt())
                    .unregisteredAt(it.getUnregisteredAt())
                    .storeId(storeEntity.get().getId())
                    .storeName(storeEntity.get().getName())
                    .build();

            return userSession;
        })
        .orElseThrow(()->new UsernameNotFoundException(username));
    }
}
