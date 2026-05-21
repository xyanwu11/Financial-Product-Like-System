package com.productlike.system.controller;

import com.productlike.system.model.dto.LikeDetailDto;
import com.productlike.system.model.dto.LikeRequest;
import com.productlike.system.model.entity.LikeItem;
import com.productlike.system.model.response.ApiResponse;
import com.productlike.system.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ApiResponse<LikeItem> addLike(@Valid @RequestBody LikeRequest request) {
        return ApiResponse.ok("新增成功", likeService.addLike(request));
    }

    @GetMapping
    public ApiResponse<List<LikeDetailDto>> getLikeList(@RequestParam String userId) {
        return ApiResponse.ok(likeService.getLikeList(userId));
    }

    @PutMapping("/{sn}")
    public ApiResponse<LikeItem> updateLike(@PathVariable Long sn,
                                            @Valid @RequestBody LikeRequest request,
                                            @RequestParam String viewerUserId) {
        return ApiResponse.ok("更新成功", likeService.updateLike(sn, request, viewerUserId));
    }

    @DeleteMapping("/{sn}")
    public ApiResponse<Void> deleteLike(@PathVariable Long sn,
                                        @RequestParam String viewerUserId) {
        likeService.deleteLike(sn, viewerUserId);
        return ApiResponse.ok("刪除成功", null);
    }
}
