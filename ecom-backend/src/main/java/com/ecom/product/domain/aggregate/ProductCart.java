package com.ecom.product.domain.aggregate;

import com.ecom.product.domain.vo.ProductBrand;
import com.ecom.product.domain.vo.ProductName;
import com.ecom.product.domain.vo.ProductPrice;
import com.ecom.product.domain.vo.PublicId;
import com.ecom.shared.error.domain.Assert;
import lombok.Getter;
import org.jilt.Builder;

@Builder
@Getter
public class ProductCart {

    private ProductName name;
    private ProductPrice price;
    private ProductBrand brand;
    private Picture picture;
    private PublicId publicId;

    public ProductCart(ProductName name, ProductPrice price, ProductBrand brand, Picture picture, PublicId publicId) {
        assertFields(name, price, brand, picture, publicId);
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.picture = picture;
        this.publicId = publicId;
    }

    private void assertFields(ProductName name, ProductPrice price, ProductBrand brand, Picture picture, PublicId publicId) {
        Assert.notNull("brand", brand);
        Assert.notNull("name", name);
        Assert.notNull("price", price);
        Assert.notNull("picture", picture);
        Assert.notNull("publicId", publicId);
    }

    public static ProductCart from(Product product){
        return ProductCartBuilder.productCart()
                .name(product.getProductName())
                .price(product.getProductPrice())
                .brand(product.getProductBrand())
                .picture(product.getPictures().stream()
                        .findFirst().orElseThrow())
                .publicId(product.getPublicId())
                .build();
    }
}
