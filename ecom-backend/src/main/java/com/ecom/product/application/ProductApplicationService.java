package com.ecom.product.application;

import com.ecom.order.domain.order.aggregate.OrderProductQuantity;
import com.ecom.product.domain.service.ProductUpdater;
import com.ecom.product.domain.aggregate.Category;
import com.ecom.product.domain.aggregate.FilterQuery;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.repository.CategoryRepository;
import com.ecom.product.domain.repository.ProductRepository;
import com.ecom.product.domain.service.CategoryCRUD;
import com.ecom.product.domain.service.ProductCRUD;
import com.ecom.product.domain.service.ProductShop;
import com.ecom.product.domain.vo.PublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class ProductApplicationService {

    private final ProductCRUD productCRUD;
    private final CategoryCRUD categoryCRUD;
    private final ProductShop productShop;
    private final ProductUpdater productUpdater;

    public ProductApplicationService(ProductRepository productRepository,
                                     CategoryRepository categoryRepository
                                     ) {
        this.productCRUD = new ProductCRUD(productRepository);
        this.categoryCRUD = new CategoryCRUD(categoryRepository);
        this.productShop=new ProductShop(productRepository);
        this.productUpdater=new ProductUpdater(productRepository);
    }

    @Transactional
    public Product createProduct(@RequestBody Product product) {
        return productCRUD.save(product);
    }

    @Transactional
    public Page<Product> findAllProducts(Pageable pageable){
        return productCRUD.findAll(pageable);
    }

    public PublicId deleteProduct(PublicId publicId){
        return productCRUD.remove(publicId);
    }

    @Transactional
    public Category createCategory(Category category){
        return categoryCRUD.save(category);
    }

    @Transactional
    public PublicId deleteCategory(PublicId publicId){
        return categoryCRUD.delete(publicId);
    }

    @Transactional(readOnly = true)
    public Page<Category> findAllCategory(Pageable pageable) {
        return categoryCRUD.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getFeaturesProducts(Pageable pageable){
        return  productShop.getFeaturedProducts(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findOne(PublicId id){
        return  productCRUD.findOne(id);
    }

    @Transactional(readOnly = true)
    public Page<Product> findRelated(Pageable pageable,PublicId productPublicId){
        return  productShop.findRelated(pageable,productPublicId);
    }

    @Transactional(readOnly = true)
    public Page<Product> filter(Pageable pageable, FilterQuery query){
        return productShop.filter(pageable,query);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByPublicIdsIn(List<PublicId> publicIds){
        return productCRUD.findAllByPublicIdIn(publicIds);
    }

    @Transactional
    public void updateProductQuantity(List<OrderProductQuantity> orderProductQuantities){
        productUpdater.updateProductQuantity(orderProductQuantities);
    }
}
