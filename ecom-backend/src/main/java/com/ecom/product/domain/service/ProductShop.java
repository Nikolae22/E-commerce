package com.ecom.product.domain.service;

import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.repository.ProductRepository;
import com.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class ProductShop {

    private final ProductRepository productRepository;


    public Page<Product> getFeaturedProducts(Pageable pageable){
        return productRepository.findAllFeaturedProduct(pageable);
    }

    public Page<Product> findRelated(Pageable pageable, PublicId productPublicId){
        Optional<Product> productOpt = productRepository.findOne(productPublicId);

        if (productOpt.isPresent()){
            Product product=productOpt.get();
            return productRepository.findByCategoryExcludingOne(pageable,
                    product.getCategory().getPublicId(),productPublicId);
        }else {
            throw new EntityNotFoundException(String.format(
                    "No product found with id %s",productPublicId
            ));
        }
    }
}
