package kr.adapterz.springboot.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostRequest {
    private Long userId;
    private String title;
    private String content;
}
