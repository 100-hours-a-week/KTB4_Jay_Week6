package kr.adapterz.springboot.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostListResponse {
    private Long postId;
    private String title;
    private String authorNickname;
    private Long commentCount;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean authorDeleted;
    private Boolean blinded;
}
