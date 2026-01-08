package com.ecom.product.domain.aggregate;

import com.ecom.product.domain.vo.CategoryName;
import com.ecom.product.domain.vo.PublicId;
import com.ecom.shared.error.domain.Assert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jilt.Builder;

import java.util.UUID;

@Builder
@RequiredArgsConstructor
@Getter
@AllArgsConstructor
public class Category {

    private final CategoryName name;

    private Long dbId;
    private PublicId publicId;

    private void assertMandatoryFields(CategoryName categoryName){
        Assert.notNull("name",categoryName);
    }

    public void initDefaultFields(){
        this.publicId=new PublicId(UUID.randomUUID());
    }
}
