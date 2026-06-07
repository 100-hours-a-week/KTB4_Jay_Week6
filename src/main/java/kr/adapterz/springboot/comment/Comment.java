package kr.adapterz.springboot.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Comment {
    @Setter
    private Long id;

    private final Long postId;
    private final Long authorId;
    private final Long parentCommentId;
    private String content;
    private boolean deleted;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment(Long postId, Long authorId, Long parentCommentId, String content) {
        this.postId = postId;
        this.authorId = authorId;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isReply() {
        return parentCommentId != null;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deleted = true;
    }
}
