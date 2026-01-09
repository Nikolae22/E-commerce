package com.ecom.product.infrastructure.secondary.repository;

import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.vo.ProductSize;
import com.ecom.product.domain.vo.PublicId;
import com.ecom.product.infrastructure.secondary.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<ProductEntity ,Long> {

    int deleteByPublicId(UUID publicId);

    Optional<ProductEntity> findByPublicId(UUID publicId);

    Page<ProductEntity> findAllByFeaturedTrue(Pageable pageable);

    Page<ProductEntity> findByCategoryPublicIdNot(Pageable pageable,
                                                  UUID categoryPublicId,
                                                  UUID excludedProductPublicId);

    @Query("select product from ProductEntity product where (:size is null or product.size in (:sizes)) and " +
            "product.category.publicId = :categoryPublicId")
    Page<ProductEntity> findByCategoryPublicIdAndSizeIn(Pageable pageable,UUID categoryPublicId, List<ProductSize> sizes);

    List<ProductEntity> findAllByPublicIdIn(List<UUID> publicIds);
}
