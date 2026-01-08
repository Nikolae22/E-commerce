package com.ecom.product.domain.aggregate;

import com.ecom.product.domain.vo.*;
import com.ecom.shared.error.domain.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jilt.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
@Getter
@AllArgsConstructor
public class Product {

    private final ProductBrand productBrand;
    private final ProductColor productColor;
    private final ProductDescription productDescription;
    private final ProductName productName;
    private final ProductPrice productPrice;
    private  final ProductSize productSize;
    private final  Category category;
    private final List<Picture> pictures;
    private Long dbId;
    private boolean featured;
    private PublicId publicId;
    private  int nbInStock;

    private void assertMandatoryFields(
            ProductBrand brand,
            ProductColor color,
            ProductDescription description,
            ProductName name,
            ProductPrice price,
            ProductSize size,
            Category category,
            List<Picture> pictures,
            boolean featured,
            int nbInStock){
        assertMandatoryFields(brand, color, description, name, price, size, category, pictures, featured, nbInStock);
        Assert.notNull("brand",brand);
        Assert.notNull("color",color);
        Assert.notNull("description",description);
        Assert.notNull("name",name);
        Assert.notNull("price",price);
        Assert.notNull("size",size);
        Assert.notNull("category",category);
        Assert.notNull("pictures",pictures);
        Assert.notNull("featured",featured);
        Assert.notNull("nbInStock",nbInStock);
    }

    public void initDefaultFields(){
        this.publicId=new PublicId(UUID.randomUUID());
    }

}
