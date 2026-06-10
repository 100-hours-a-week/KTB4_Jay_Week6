package kr.adapterz.springboot.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Report {
    @Setter
    private Long id;

    private final Long postId;
    private final Long userId;
    private final String reason;
    private final LocalDateTime createdAt;

    public Report(Long postId, Long userId, String reason) {
        this.postId = postId;
        this.userId = userId;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }
}
