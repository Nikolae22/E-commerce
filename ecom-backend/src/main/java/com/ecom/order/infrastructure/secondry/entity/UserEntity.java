package com.ecom.order.infrastructure.secondry.entity;

import com.ecom.order.domain.user.aggregate.User;
import com.ecom.order.domain.user.aggregate.UserBuilder;
import com.ecom.order.domain.user.vo.*;
import com.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jilt.Builder;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "ecommerce_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "userSequenceGenerator")
    @SequenceGenerator(name = "userSequenceGenerator",sequenceName = "user_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    private String firstName;

    private String email;

    private String imageUrl;
    private UUID publicId;
    private String addressStreet;
    private String addressCity;
    private String addressZipCode;
    private String addressCountry;
    private Instant lastSeen;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_authority",
            joinColumns ={@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(name = "authority_name",referencedColumnName = "name")}
    )
    private Set<AuthorityEntity> authorities=new HashSet<>();

    public void  updateFromUser(User user){
        this.email=user.getEmail().value();
        this.lastName=user.getLastname().value();
        this.firstName=user.getFirstname().value();
        this.imageUrl=user.getImageUrl().value();
        this.lastSeen=user.getLastSeen();
    }

    public static UserEntity from(User user){

        UserEntityBuilder userEntityBuilder=UserEntityBuilder.userEntity();
        if (user.getImageUrl() !=null){
            userEntityBuilder.imageUrl(user.getImageUrl().value());
        }

        if (user.getUserPublicId() != null){
            userEntityBuilder.publicId(user.getUserPublicId().value());
        }

        if (user.getUserAddress() !=null){
            userEntityBuilder.addressCity(user.getUserAddress().city());
            userEntityBuilder.addressCountry(user.getUserAddress().country());
            userEntityBuilder.addressZipCode(user.getUserAddress().zipCode());
            userEntityBuilder.addressStreet(user.getUserAddress().street());
        }
        return  userEntityBuilder
                .authorities(AuthorityEntity.from(user.getAuthorities()))
                .email(user.getEmail().value())
                .firstName(user.getFirstname().value())
                .lastName(user.getLastname().value())
                .lastSeen(user.getLastSeen())
                .id(user.getDbId())
                .build();
    }

    public static User toDomain(UserEntity userEntity){
        UserBuilder userBuilder=UserBuilder.user();
        if (userEntity.getImageUrl() !=null){
            userBuilder.imageUrl(new UserImageUrl(userEntity.getImageUrl()));
        }

        if (userEntity.getAddressStreet() !=null){
            userBuilder.userAddress(
                    UserAddressBuilder.userAddress()
                            .city(userEntity.getAddressCity())
                            .country(userEntity.getAddressCountry())
                            .zipCode(userEntity.getAddressZipCode())
                            .street(userEntity.getAddressStreet())
                            .build()
            );
        }

        return userBuilder
                .email(new UserEmail(userEntity.getEmail()))
                .lastname(new UserLastname(userEntity.getLastName()))
                .firstname(new UserFirstname(userEntity.getFirstName()))
                .authorities(AuthorityEntity.toDomain(userEntity.getAuthorities()))
                .userPublicId(new UserPublicId(userEntity.getPublicId()))
                .lastModifiedDate(userEntity.getLastModifiedDate())
                .createdDate(userEntity.getCreatedDate())
                .dbId(userEntity.getId())
                .build();
    }

    public static Set<UserEntity> from(List<User> users){
        return users.stream().map(
                UserEntity::from
        ).collect(Collectors.toSet());
    }

    public static Set<User> toDomain(List<UserEntity> users){
        return users.stream().map(UserEntity::toDomain)
                .collect(Collectors.toSet());
    }
}
