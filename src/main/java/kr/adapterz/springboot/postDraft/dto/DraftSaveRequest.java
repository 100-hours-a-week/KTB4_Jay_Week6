package kr.adapterz.springboot.postDraft.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DraftSaveRequest {

    private Long userId;
    private String title;
    private String content;
}
