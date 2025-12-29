package com.ecom.order.domain.user.service;

import com.ecom.order.domain.user.aggregate.User;
import com.ecom.order.domain.user.repository.UserRepository;
import com.ecom.order.domain.user.vo.UserEmail;
import com.ecom.order.domain.user.vo.UserPublicId;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public Optional<User> geByEmail(UserEmail userEmail){
        return  userRepository.getOneByEmail(userEmail);
    }

    public Optional<User> getByPublicId(UserPublicId userPublicId){
        return  userRepository.get(userPublicId);
    }
}
