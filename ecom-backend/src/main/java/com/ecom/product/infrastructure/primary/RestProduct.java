package com.ecom.product.infrastructure.primary;

import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.aggregate.ProductBuilder;
import com.ecom.product.domain.vo.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestProduct {

    private String brand;
    private String color;
    private String description;
    private String name;
    private double price;
    private ProductSize size;
    private RestCategory category;
    private boolean featured;
    private List<RestPicture> pictures;
    private UUID publicId;
    private int nbInStock;


    public void addPictureAttachment(List<RestPicture> pictures){
        this.pictures.addAll(pictures);
    }

    public static Product toDomain(RestProduct restProduct){
        ProductBuilder productBuilder=ProductBuilder.product()
                .productBrand(new ProductBrand(restProduct.getBrand()))
                .productColor(new ProductColor(restProduct.getColor()))
                .productDescription(new ProductDescription(restProduct.getDescription()))
                .productName(new ProductName(restProduct.getName()))
                .productPrice(new ProductPrice(restProduct.getPrice()))
                .productSize(restProduct.getSize())
                .category(RestCategory.toDomain(restProduct.getCategory()))
                .featured(restProduct.isFeatured())
                .nbInStock(restProduct.getNbInStock());

        if (restProduct.publicId !=null){
            productBuilder.publicId(new PublicId(restProduct.publicId));
        }

        if (restProduct.pictures !=null && !restProduct.pictures.isEmpty()){
            productBuilder.pictures(RestPicture.toDomain(restProduct.getPictures()));
        }

        return productBuilder.build();
    }


    public static RestProduct fromDomain(Product product){
        return RestProductBuilder.restProduct()
                .brand(product.getProductBrand().value())
                .color(product.getProductColor().value())
                .description(product.getProductDescription().value())
                .name(product.getProductName().value())
                .price(product.getProductPrice().value())
                .featured(product.isFeatured())
                .category(RestCategory.fromDomain(product.getCategory()))
                .size(product.getProductSize())
                .pictures(RestPicture.fromDomain(product.getPictures()))
                .publicId(product.getPublicId().value())
                .nbInStock(product.getNbInStock())
                .build();
    }


}
