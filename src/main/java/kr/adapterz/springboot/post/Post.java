package kr.adapterz.springboot.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class Post {
    @Setter
    private Long id;

    private final Long authorId;
    private String title;
    private String content;
    private int viewCount;
    private boolean blinded;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
    private boolean edited;

    public Post(Long authorId,String title, String content){
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.blinded = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deleted = false;
        this.edited = false;
    }

    public void increaseViewCount(){
        this.viewCount ++;
    }

    // 신고 누적 5번 이상이면 blind 함수 실행
    public void blind(){
        this.blinded = true;
    }

    // 게시글 수정 시 제목과 내용 변경
    public void update(String title, String content){
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
        this.edited = true;
    }

    public void delete(){
        this.deleted = true;
    }


}
