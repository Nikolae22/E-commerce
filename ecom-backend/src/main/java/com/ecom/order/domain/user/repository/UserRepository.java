package com.ecom.order.domain.user.repository;

import com.ecom.order.domain.user.aggregate.User;
import com.ecom.order.domain.user.vo.UserAddress;
import com.ecom.order.domain.user.vo.UserAddressToUpdate;
import com.ecom.order.domain.user.vo.UserEmail;
import com.ecom.order.domain.user.vo.UserPublicId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {

    void save(User user);

    Optional<User> get(UserPublicId userPublicId);

    Optional<User> getOneByEmail(UserEmail userEmail);

    void updateAddress(UserPublicId userPublicId, UserAddressToUpdate userAddress);
}
