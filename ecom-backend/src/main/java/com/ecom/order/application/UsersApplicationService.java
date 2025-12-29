package com.ecom.order.application;

import com.ecom.order.domain.user.aggregate.User;
import com.ecom.order.domain.user.repository.UserRepository;
import com.ecom.order.domain.user.service.UserReader;
import com.ecom.order.domain.user.service.UserSynchronizer;
import com.ecom.order.domain.user.vo.UserAddressToUpdate;
import com.ecom.order.domain.user.vo.UserEmail;
import com.ecom.order.infrastructure.secondry.service.kinde.KindeService;
import com.ecom.shared.authentication.application.AuthenticatedUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersApplicationService {

    private final UserSynchronizer userSynchronizer;
    private final UserReader userReader;

    public UsersApplicationService(UserRepository userRepository, KindeService kindeService) {
        this.userSynchronizer=new UserSynchronizer(userRepository, kindeService);
        this.userReader=new UserReader(userRepository);
    }

    @Transactional
    public User getAuthenticatedUserWithSync(Jwt jwtToken,boolean forceSync){
        userSynchronizer.syncWithIdp(jwtToken,forceSync);
        return userReader.geByEmail(new UserEmail(
                AuthenticatedUser.username().get()
        )).orElseThrow();
    }

    @Transactional
    public User getAutheniticatedUser(){
        return  userReader.geByEmail(new UserEmail(
                AuthenticatedUser.username().get()
        )).orElseThrow();
    }

    @Transactional
    public void updateAddress(UserAddressToUpdate userAddressToUpdate){
        userSynchronizer.updateAddress(userAddressToUpdate);
    }
}
