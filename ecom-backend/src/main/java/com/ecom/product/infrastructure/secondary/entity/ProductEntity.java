package com.ecom.product.infrastructure.secondary.entity;

import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.aggregate.ProductBuilder;
import com.ecom.product.domain.vo.*;
import com.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jilt.Builder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "product")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
public class ProductEntity extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "productSequence")
    @SequenceGenerator(name = "productSequence",sequenceName = "picture_sequence",allocationSize = 3)
    private Long id;

    private String brand;

    private String color;
    private String description;
    private String name;
    private double price;
    private boolean featured;
    @Enumerated(EnumType.STRING)
    private ProductSize size;

    @Column(unique = true)
    private UUID publicId;

    @Column(name = "nb_in_stock")
    private int nbInStock;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "product")
    private Set<PictureEntity> pictures=new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_fk", referencedColumnName = "id")
    private CategoryEntity category;

    public ProductEntity() {
    }

    public ProductEntity(Long id, String brand, String color, String description, String name, double price, boolean featured, ProductSize size, UUID publicId, int nbInStock, Set<PictureEntity> pictures) {
        this.id = id;
        this.brand = brand;
        this.color = color;
        this.description = description;
        this.name = name;
        this.price = price;
        this.featured = featured;
        this.size = size;
        this.publicId = publicId;
        this.nbInStock = nbInStock;
        this.pictures = pictures;
    }

    public static ProductEntity from(Product product){
        ProductEntityBuilder productEntityBuilder=ProductEntityBuilder.productEntity();

        if (product.getDbId() !=null){
            productEntityBuilder.id(product.getDbId());
        }

        return productEntityBuilder
                .brand(product.getProductBrand().value())
                .color(product.getProductColor().value())
                .description(product.getProductDescription().value())
                .name(product.getProductName().value())
                .price(product.getProductPrice().value())
                .size(product.getProductSize())
                .publicId(product.getPublicId().value())
                .category(CategoryEntity.from(product.getCategory()))
                .pictures(PictureEntity.from(product.getPictures()))
                .featured(product.isFeatured())
                .nbInStock(product.getNbInStock())
                .build();
    }

    public static Product to(ProductEntity productEntity){
        return ProductBuilder.product()
                .productBrand(new ProductBrand(productEntity.getBrand()))
                .productColor(new ProductColor(productEntity.getColor()))
                .productDescription(new ProductDescription(productEntity.getDescription()))
                .productName(new ProductName(productEntity.getName()))
                .productPrice(new ProductPrice(productEntity.getPrice()))
                .productSize(productEntity.size)
                .publicId(new PublicId(productEntity.getPublicId()))
                .category(CategoryEntity.to(productEntity.getCategory()))
                .pictures(PictureEntity.to(productEntity.getPictures()))
                .featured(productEntity.isFeatured())
                .nbInStock(productEntity.getNbInStock())
                .build();
    }
}
