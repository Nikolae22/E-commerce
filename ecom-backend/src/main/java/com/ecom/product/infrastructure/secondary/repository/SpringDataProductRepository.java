package com.ecom.product.infrastructure.secondary.repository;

import com.ecom.product.domain.aggregate.FilterQuery;
import com.ecom.product.domain.aggregate.Picture;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.repository.ProductRepository;
import com.ecom.product.domain.vo.PublicId;
import com.ecom.product.infrastructure.secondary.entity.CategoryEntity;
import com.ecom.product.infrastructure.secondary.entity.PictureEntity;
import com.ecom.product.infrastructure.secondary.entity.ProductEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class SpringDataProductRepository implements ProductRepository {

    private final JpaCategoryRepository jpaCategoryRepository;
    private final JpaProductRepository jpaProductRepository;
    private final JpaProductPicturesRepository jpaProductPicturesRepository;

    @Override
    public Product save(Product productToCreate) {
        ProductEntity newProductEntity = ProductEntity.from(productToCreate);
        Optional<CategoryEntity> categoryEntityOpt = jpaCategoryRepository.findByPublicId(newProductEntity.getCategory().getPublicId());
        CategoryEntity categoryEntity = categoryEntityOpt.orElseThrow(() -> new EntityNotFoundException(
                String.format("No category found with Id %s", productToCreate.getCategory().getPublicId())
        ));
        newProductEntity.setCategory(categoryEntity);
        ProductEntity savedProductEntity = jpaProductRepository.save(newProductEntity);

        savedAllPictures(productToCreate.getPictures(),savedProductEntity);

        return ProductEntity.to(savedProductEntity);
    }

    private void savedAllPictures(List<Picture> pictures,ProductEntity newProductEntity){
        Set<PictureEntity> pictureEntities = PictureEntity.from(pictures);
        for (PictureEntity pictureEntity:pictureEntities){
            pictureEntity.setProduct(newProductEntity);
        }

        jpaProductPicturesRepository.saveAll(pictureEntities);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaProductRepository.findAll(pageable).map(ProductEntity::to);
    }

    @Override
    public int delete(PublicId publicId) {
        return jpaProductRepository.deleteByPublicId(publicId.value());
    }


    @Override
    public Page<Product> findAllFeaturedProduct(Pageable pageable) {
        return jpaProductRepository.findAllByFeaturedTrue(pageable)
                .map(ProductEntity::to);
    }

    @Override
    public Optional<Product> findOne(PublicId publicId) {
        return jpaProductRepository.findByPublicId(publicId.value())
                .map(ProductEntity::to);
    }

    @Override
    public Page<Product> findByCategoryExcludingOne(Pageable pageable, PublicId categoryPublicId, PublicId productPublicId) {
        return jpaProductRepository.findByCategoryPublicIdNot(pageable,
                categoryPublicId.value(),productPublicId.value())
                .map(ProductEntity::to);
    }

    @Override
    public Page<Product> findByCategoryAndSize(Pageable pageable, FilterQuery filterQuery) {
        return jpaProductRepository.findByCategoryPublicIdAndSizeIn(
                pageable,filterQuery.categoryId().value(),
                filterQuery.sizes()
        ).map(ProductEntity::to);
    }
}
