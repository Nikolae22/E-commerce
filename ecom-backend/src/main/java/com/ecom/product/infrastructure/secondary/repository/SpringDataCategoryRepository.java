package com.ecom.product.infrastructure.secondary.repository;

import com.ecom.product.domain.aggregate.Category;
import com.ecom.product.domain.repository.CategoryRepository;
import com.ecom.product.domain.vo.PublicId;
import com.ecom.product.infrastructure.secondary.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SpringDataCategoryRepository implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return jpaCategoryRepository.findAll(pageable).map(
                CategoryEntity::to);
    }

    @Override
    public int delete(PublicId publicId) {
        return jpaCategoryRepository.deleteByPublicId(publicId.value());

    }

    @Override
    public Category save(Category categoryToCreate) {
        CategoryEntity categoryToSave = CategoryEntity.from(categoryToCreate);
        CategoryEntity categorySaved = jpaCategoryRepository.save(categoryToSave);
        return CategoryEntity.to(categorySaved);
    }
}
