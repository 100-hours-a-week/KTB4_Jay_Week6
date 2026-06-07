package kr.adapterz.springboot.like;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Like {
    @Setter
    private Long id;

    private final Long postId;
    private final Long userId;

    public Like(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
