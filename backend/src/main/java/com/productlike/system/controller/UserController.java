package com.productlike.system.controller;

import com.productlike.system.model.dto.AuthUserDto;
import com.productlike.system.model.dto.LoginRequest;
import com.productlike.system.model.dto.RegisterRequest;
import com.productlike.system.model.response.ApiResponse;
import com.productlike.system.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final LikeService likeService;

    public UserController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthUserDto> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("登入成功", likeService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<AuthUserDto> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok("註冊成功", likeService.register(request));
    }
}
