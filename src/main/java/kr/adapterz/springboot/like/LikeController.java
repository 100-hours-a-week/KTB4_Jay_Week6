package kr.adapterz.springboot.like;

import kr.adapterz.springboot.global.ApiResponse;
import kr.adapterz.springboot.like.dto.LikeRequest;
import kr.adapterz.springboot.like.dto.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<ApiResponse<LikeResponse>> like(
            @PathVariable Long postId,
            @RequestBody LikeRequest request
    ) {
        LikeResponse response = likeService.like(postId, request);

        return ResponseEntity.ok()
                .body(new ApiResponse<>("like_success", response));
    }

    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity<ApiResponse<LikeResponse>> unlike(
            @PathVariable Long postId,
            @RequestBody LikeRequest request
    ) {
        LikeResponse response = likeService.unlike(postId, request);

        return ResponseEntity.ok()
                .body(new ApiResponse<>("unlike_success", response));
    }
}
