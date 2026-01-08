package com.ecom.product.infrastructure.secondary.entity;

import com.ecom.product.domain.aggregate.Picture;
import com.ecom.product.domain.aggregate.PictureBuilder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jilt.Builder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "product_picture")
@Builder
@Getter
@Setter
public class PictureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pictureSequence")
    @SequenceGenerator(name = "pictureSequence",sequenceName = "picture_category_sequence",allocationSize = 3)
    private Long id;

    @Lob
    @Column(name = "file",nullable = false)
    private byte[] file;

    @Column(name = "file_content_type",nullable = false)
    private String mimeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_fk",nullable = false)
    private ProductEntity product;

    public PictureEntity() {
    }

    public PictureEntity(Long id, byte[] file, String mimeType, ProductEntity product) {
        this.id = id;
        this.file = file;
        this.mimeType = mimeType;
        this.product = product;
    }

    public static PictureEntity from(Picture picture){
       return PictureEntityBuilder.pictureEntity()
                .file(picture.file())
                .mimeType(picture.mimeType())
                .build();
    }

    public static Picture to(PictureEntity pictureEntity){
        return PictureBuilder.picture()
                .file(pictureEntity.getFile())
                .mimeType(pictureEntity.getMimeType())
                .build();
    }

    public static Set<PictureEntity> from(List<Picture> pictures){
        return pictures.stream()
                .map(PictureEntity::from)
                .collect(Collectors.toSet());
    }

    public static  List<Picture> to (Set<PictureEntity> pictureEntities){
        return pictureEntities.stream()
                .map(PictureEntity::to)
                .collect(Collectors.toList());
    }
}
