package com.ecom.order.infrastructure.secondry.repository;

import com.ecom.order.domain.user.aggregate.User;
import com.ecom.order.domain.user.repository.UserRepository;
import com.ecom.order.domain.user.vo.UserAddressToUpdate;
import com.ecom.order.domain.user.vo.UserEmail;
import com.ecom.order.domain.user.vo.UserPublicId;
import com.ecom.order.infrastructure.secondry.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        if (user.getDbId() !=null){
            Optional<UserEntity> userToUpdateToDpt = jpaUserRepository.findById(user.getDbId());
            if (userToUpdateToDpt.isPresent()){
                UserEntity userToUpdate=userToUpdateToDpt.get();
                userToUpdate.updateFromUser(user);
                jpaUserRepository.saveAndFlush(userToUpdate);
            }else {
                jpaUserRepository.save(UserEntity.from(user));
            }
        }
    }

    @Override
    public Optional<User> get(UserPublicId userPublicId) {
        return jpaUserRepository.findOneByPublicId(userPublicId.value())
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> getOneByEmail(UserEmail userEmail) {
        return jpaUserRepository.findByEmail(userEmail.value())
                .map(UserEntity::toDomain);
    }

    @Override
    public void updateAddress(UserPublicId userPublicId, UserAddressToUpdate userAddress) {
        jpaUserRepository.updateAddress(userPublicId.value(),
                userAddress.userAddress().street(),
                userAddress.userAddress().city(),
                userAddress.userAddress().country(),
                userAddress.userAddress().zipCode()
        );

    }
}
