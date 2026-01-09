package com.ecom.product.domain.service;

import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.repository.ProductRepository;
import com.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductCRUD {

    private final ProductRepository productRepository;

    public Product save(Product newProduct){
        newProduct.initDefaultFields();
        return  productRepository.save(newProduct);
    }

    public Page<Product> findAll(Pageable pageable){
        return  productRepository.findAll(pageable);
    }

    public PublicId remove(PublicId id){
        int nbOfRowsDeleted = productRepository.delete(id);
        if (nbOfRowsDeleted !=1){
            throw new EntityNotFoundException(String.format(
                    "No Category deleted with id %s",id
            ));
        }
        return id;
    }

    public Optional<Product> findOne(PublicId publicId){
        return productRepository.findOne(publicId);
    }

    public List<Product> findAllByPublicIdIn(List<PublicId> publicIds){
        return productRepository.findByPublicIds(publicIds);
    }
}
