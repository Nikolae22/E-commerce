package com.ecom.product.domain.service;

import com.ecom.product.domain.aggregate.Category;
import com.ecom.product.domain.repository.CategoryRepository;
import com.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CategoryCRUD {

    private final CategoryRepository categoryRepository;


    public Category save(Category category){
        category.initDefaultFields();
        return categoryRepository.save(category);
    }

    public Page<Category> findAll(Pageable pageable){
        return  categoryRepository.findAll(pageable);
    }

    public PublicId delete(PublicId publicId){
        int nbOfRowsDeleted = categoryRepository.delete(publicId);
        if (nbOfRowsDeleted !=1){
            throw new EntityNotFoundException(String.format("No category delete with id %s",publicId));
        }
        return publicId;
    }
}
