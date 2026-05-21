package com.productlike.system.controller;

import com.productlike.system.model.entity.Product;
import com.productlike.system.model.response.ApiResponse;
import com.productlike.system.service.LikeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final LikeService likeService;

    public ProductController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public ApiResponse<List<Product>> getAllProducts() {
        return ApiResponse.ok(likeService.getAllProducts());
    }
}
