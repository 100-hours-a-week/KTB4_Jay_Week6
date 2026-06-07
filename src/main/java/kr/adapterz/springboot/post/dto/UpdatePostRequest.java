package kr.adapterz.springboot.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdatePostRequest {
    private String title;
    private String content;
}
