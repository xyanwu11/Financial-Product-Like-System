package com.productlike.system.service;

import com.productlike.system.model.dto.AuthUserDto;
import com.productlike.system.model.dto.LikeDetailDto;
import com.productlike.system.model.dto.LikeRequest;
import com.productlike.system.model.dto.LoginRequest;
import com.productlike.system.model.dto.RegisterRequest;
import com.productlike.system.model.entity.LikeItem;
import com.productlike.system.model.entity.Product;
import com.productlike.system.model.entity.User;
import com.productlike.system.repository.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public List<User> getAllUsers() {
        return likeRepository.findAllUsers();
    }

    @Transactional
    public AuthUserDto register(RegisterRequest request) {
        if (likeRepository.findUserById(request.getUserId()).isPresent()) {
            throw new IllegalArgumentException("使用者 ID 已存在");
        }
        User user = likeRepository.createUser(
                request.getUserId(),
                request.getUserName(),
                request.getEmail(),
                request.getAccount(),
                request.getPassword()
        );
        return toAuthUser(user);
    }

    public AuthUserDto login(LoginRequest request) {
        User user = likeRepository.findUserByCredentials(request.getUserId(), request.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("使用者 ID 或密碼錯誤"));
        return toAuthUser(user);
    }

    public List<Product> getAllProducts() {
        return likeRepository.findAllProducts();
    }

    @Transactional
    public LikeItem addLike(LikeRequest request) {
        User user = requireUser(request.getUserId());
        return likeRepository.insertLike(
                user.getUserId(),
                request.getProductNo(),
                request.getPurchaseQuantity(),
                user.getAccount()
        );
    }

    public List<LikeDetailDto> getLikeList(String viewerUserId) {
        User viewer = requireUser(viewerUserId);
        if (isAdmin(viewer)) {
            return likeRepository.getAllLikeList();
        }
        return likeRepository.getLikeList(viewer.getUserId());
    }

    @Transactional
    public void deleteLike(Long sn, String viewerUserId) {
        if (sn == null) {
            throw new IllegalArgumentException("收藏序號不可為空");
        }
        ensureCanAccessLike(sn, viewerUserId);
        likeRepository.deleteLike(sn);
    }

    @Transactional
    public LikeItem updateLike(Long sn, LikeRequest request, String viewerUserId) {
        if (sn == null) {
            throw new IllegalArgumentException("收藏序號不可為空");
        }
        User viewer = requireUser(viewerUserId);
        ensureCanAccessLike(sn, viewerUserId);
        return likeRepository.updateLike(
                sn,
                request.getProductNo(),
                request.getProductName(),
                request.getPrice(),
                request.getFeeRate(),
                request.getPurchaseQuantity(),
                isAdmin(viewer) ? request.getAccount() : viewer.getAccount()
        );
    }

    private void ensureCanAccessLike(Long sn, String viewerUserId) {
        User viewer = requireUser(viewerUserId);
        if (isAdmin(viewer)) return;

        String ownerUserId = likeRepository.findLikeOwner(sn)
                .orElseThrow(() -> new IllegalArgumentException("找不到收藏資料"));
        if (!ownerUserId.equals(viewer.getUserId())) {
            throw new IllegalArgumentException("一般使用者只能操作自己的商品");
        }
    }

    private User requireUser(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("請先登入");
        }
        return likeRepository.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("找不到登入使用者"));
    }

    private boolean isAdmin(User user) {
        return "ADMIN".equalsIgnoreCase(user.getRole());
    }

    private AuthUserDto toAuthUser(User user) {
        return new AuthUserDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getAccount(),
                user.getRole()
        );
    }
}
