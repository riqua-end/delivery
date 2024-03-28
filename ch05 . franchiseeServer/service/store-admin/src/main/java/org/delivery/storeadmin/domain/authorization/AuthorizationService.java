package org.delivery.storeadmin.domain.authorization;

import lombok.RequiredArgsConstructor;
import org.delivery.storeadmin.domain.user.service.StoreUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final StoreUserService storeUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var storeUserEntity =  storeUserService.getRegisterUser(username);

        return storeUserEntity.map(it->{
            var user = User.builder()
                    .username(it.getEmail())
                    .password(it.getPassword())
                    .roles(it.getRole().toString())
                    .build();
            return user;
        })
        .orElseThrow(()->new UsernameNotFoundException(username));
    }
}
