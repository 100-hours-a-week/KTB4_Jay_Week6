package kr.adapterz.springboot.post.dto;

import kr.adapterz.springboot.comment.dto.CommentDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private String authorNickname;
    private String authorProfileImage;
    private Boolean authorDeleted;
    private Boolean blinded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Long viewCount;
    private Long commentCount;
    private List<CommentDetailResponse> comments;
}
