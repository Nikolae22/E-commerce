package com.ecom.product.infrastructure.primary;

import com.ecom.product.application.ProductApplicationService;
import com.ecom.product.domain.aggregate.FilterQueryBuilder;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.vo.ProductSize;
import com.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/products-shop")
@RequiredArgsConstructor
public class ProductsShopResource {

    private final ProductApplicationService productApplicationService;

    @GetMapping("/featured")
    public ResponseEntity<Page<RestProduct>> getAllFeatured(Pageable pageable){
        Page<Product> products=productApplicationService.getFeaturesProducts(pageable);

        PageImpl<RestProduct> restProducts = new PageImpl<>(
                products.getContent().stream()
                        .map(RestProduct::fromDomain)
                        .collect(Collectors.toList()),
                pageable,
                products.getTotalElements()
        );
        return ResponseEntity.ok(restProducts);
    }

    @GetMapping("/find-one")
    public ResponseEntity<RestProduct> getOne(@RequestParam("publicId") UUID id){
        Optional<Product> productOpt = productApplicationService.findOne(new PublicId(id));

        return productOpt.map(product1 -> ResponseEntity.ok(RestProduct.fromDomain(product1)))
        .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @GetMapping("/related")
    public ResponseEntity<Page<RestProduct>> findRelated(Pageable pageable,
                                                         @RequestParam("publicId") UUID id){
        try {
            Page<Product> products = productApplicationService.findRelated(pageable, new PublicId(id));
            PageImpl<RestProduct> restProducts = new PageImpl<>(
                    products.getContent().stream().map(RestProduct::fromDomain).toList(),
                    pageable, products.getTotalElements()
            );
            return ResponseEntity.ok(restProducts);
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<RestProduct>> filter(Pageable pageable,
                                                    @RequestParam("categoryId")UUID categoryId,
                                                    @RequestParam(value = "productSizes",required = false)
                                                    List<ProductSize> productSizes){
        FilterQueryBuilder filterQueryBuilder=FilterQueryBuilder.filterQuery().categoryId(new PublicId(categoryId));
        if (productSizes !=null){
            filterQueryBuilder.sizes(productSizes);
        }

        Page<Product> products = productApplicationService.filter(pageable, filterQueryBuilder.build());
        PageImpl<RestProduct> restProducts = new PageImpl<>(
                products.getContent().stream().map(RestProduct::fromDomain).toList(),
                pageable, products.getTotalElements()
        );
        return ResponseEntity.ok(restProducts);
    }
}
