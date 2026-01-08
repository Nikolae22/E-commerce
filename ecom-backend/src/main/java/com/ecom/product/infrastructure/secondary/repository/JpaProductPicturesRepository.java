package com.ecom.product.infrastructure.secondary.repository;

import com.ecom.product.infrastructure.secondary.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.parser.Entity;

public interface JpaProductPicturesRepository extends JpaRepository<PictureEntity,Long> {
}
