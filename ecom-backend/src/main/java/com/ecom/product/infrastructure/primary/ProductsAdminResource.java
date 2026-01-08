package com.ecom.product.infrastructure.primary;

import com.ecom.product.application.ProductApplicationService;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.vo.PublicId;
import com.ecom.product.infrastructure.primary.exception.MultipartPictureException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import tools.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductsAdminResource {

    private final ProductApplicationService productApplicationService;
    private final static String ROLE_ADMIN="ROLE_ADMIN";

    private  final ObjectMapper objectMapper=new ObjectMapper();


    public ProductsAdminResource(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @PreAuthorize("hasAnyRole('"+ ROLE_ADMIN +"')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestProduct> save(MultipartHttpServletRequest request,
                                            @RequestPart("dto")String productRaw){
    List<RestPicture> pictures= request.getFileMap().values()
            .stream()
            .map(mapMultipartFileToRestPictures())
            .toList();

        RestProduct restProduct= objectMapper.readValue(productRaw, RestProduct.class);
        restProduct.addPictureAttachment(pictures);
        Product newProduct=RestProduct.toDomain(restProduct);
        Product product = productApplicationService.createProduct(newProduct);
        return ResponseEntity.ok(RestProduct.fromDomain(product));

    }

    private Function<MultipartFile , RestPicture> mapMultipartFileToRestPictures(){
        return  multipartFile -> {
            try {
                return new RestPicture(multipartFile.getBytes(),multipartFile.getContentType());
            }catch (IOException e){
                throw new MultipartPictureException(String.format(
                        "Cannot parse multipart file %s",multipartFile
                ));
            }
        };
    }

    @PreAuthorize("hasAnyRole('"+ ROLE_ADMIN +"')")
    @DeleteMapping
    public ResponseEntity<UUID> delete(@RequestParam("publicId")UUID id){
        try {
            PublicId publicId = productApplicationService.deleteProduct(new PublicId(id));
            return ResponseEntity.ok(publicId.value());
        }catch (EntityNotFoundException e){
            log.error("Caluld not delete category with id {}",id,e);
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
    public ResponseEntity<Page<RestProduct>> getAll(Pageable pageable) {
        Page<Product> products = productApplicationService.findAllProducts(pageable);

        Page<RestProduct> restProducts = new PageImpl<>(
                products.getContent().stream()
                        .map(RestProduct::fromDomain)
                        .collect(Collectors.toList()),
                pageable,
                products.getTotalElements()
        );
        return ResponseEntity.ok(restProducts);
    }

}
