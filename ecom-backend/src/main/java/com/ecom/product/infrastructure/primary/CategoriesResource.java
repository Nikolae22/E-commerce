package com.ecom.product.infrastructure.primary;

import com.ecom.product.application.ProductApplicationService;
import com.ecom.product.domain.aggregate.Category;
import com.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoriesResource {

    private final ProductApplicationService productApplicationService;
    private final static String ROLE_ADMIN="ROLE_ADMIN";


    @PostMapping
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
    public ResponseEntity<RestCategory> save(@RequestBody RestCategory restCategory) {
        Category categoryDomain = RestCategory.toDomain(restCategory);
        Category categorySaved = productApplicationService.createCategory(categoryDomain);
        return ResponseEntity.ok(RestCategory.fromDomain(categorySaved));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
    public ResponseEntity<UUID> delete(UUID publicId) {
        try {
            PublicId deletedCategoryPublicId = productApplicationService.deleteCategory(new PublicId(publicId));
            return ResponseEntity.ok(deletedCategoryPublicId.value());
        } catch (EntityNotFoundException e) {
            log.error("Could not delete category with id {}", publicId, e);
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<RestCategory>> findAll(Pageable pageable) {
        Page<Category> categories = productApplicationService.findAllCategory(pageable);
        PageImpl<RestCategory> restCategories = new PageImpl<>(
                categories.getContent().stream().map(RestCategory::fromDomain).toList(),
                pageable,
                categories.getTotalElements()
        );
        return ResponseEntity.ok(restCategories);
    }
}
