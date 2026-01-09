package com.ecom.order.application;

import com.ecom.order.domain.order.aggregate.DetailCartItemRequest;
import com.ecom.order.domain.order.aggregate.DetailCartRequest;
import com.ecom.order.domain.order.aggregate.DetailCartResponse;
import com.ecom.order.domain.order.service.CartReader;
import com.ecom.product.application.ProductApplicationService;
import com.ecom.product.domain.aggregate.Product;
import com.ecom.product.domain.vo.PublicId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderApplicationService {
    private final ProductApplicationService productApplicationService;
    private final CartReader cartReader;

    public OrderApplicationService(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
        this.cartReader = new CartReader();
    }

    @Transactional(readOnly = true)
    public DetailCartResponse getCartDetails(DetailCartRequest detailCartRequest){
        List<PublicId> publicIds = detailCartRequest.items().stream().map(DetailCartItemRequest::publicId).toList();
        List<Product> productInformation = productApplicationService.getProductsByPublicIdsIn(publicIds);
        return cartReader.getDetails(productInformation);
    }
}
