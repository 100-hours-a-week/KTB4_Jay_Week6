package kr.adapterz.springboot.post;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PostViewRepository {
    private final Map<String, LocalDateTime> store = new HashMap<>();

    public Optional<LocalDateTime> findViewedAt(Long postId, Long userId) {
        return Optional.ofNullable(store.get(createKey(postId, userId)));
    }

    public void save(Long postId, Long userId, LocalDateTime viewedAt) {
        store.put(createKey(postId, userId), viewedAt);
    }

    private String createKey(Long postId, Long userId) {
        return postId + ":" + userId;
    }
}
