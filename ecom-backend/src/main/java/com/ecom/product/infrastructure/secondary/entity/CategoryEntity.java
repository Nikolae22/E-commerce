package com.ecom.product.infrastructure.secondary.entity;

import com.ecom.product.domain.aggregate.Category;
import com.ecom.product.domain.aggregate.CategoryBuilder;
import com.ecom.product.domain.vo.CategoryName;
import com.ecom.product.domain.vo.PublicId;
import com.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jilt.Builder;

import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "product_category")
@Builder
public class CategoryEntity extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "categorySequence")
    @SequenceGenerator(name = "categorySequence",sequenceName = "product_category_sequence",allocationSize = 3)
    private  Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true,nullable = false)
    private UUID publicId;

    @OneToMany(mappedBy = "category")
    private Set<ProductEntity> products;

    public CategoryEntity(Long id, String name, UUID publicId, Set<ProductEntity> products) {
        this.id = id;
        this.name = name;
        this.publicId = publicId;
        this.products = products;
    }

    public static CategoryEntity from(Category category){
        CategoryEntityBuilder categoryEntityBuilder=CategoryEntityBuilder.categoryEntity();

        if (category.getDbId() !=null){
            categoryEntityBuilder.id(category.getDbId());
        }
    return categoryEntityBuilder
            .name(category.getName().value())
            .publicId(category.getPublicId().value())
            .build();

    }

    public static Category to(CategoryEntity categoryEntity){
        return CategoryBuilder.category()
                .dbId(categoryEntity.getId())
                .name(new CategoryName(categoryEntity.getName()))
                .publicId(new PublicId(categoryEntity.getPublicId()))
                .build();
    }

}
