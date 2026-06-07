package kr.adapterz.springboot.like;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LikeRepository {
    private final Map<Long, Like> store = new HashMap<>();
    private Long sequence = 1L;

    public Like save(Like like) {
        like.setId(sequence);
        store.put(sequence, like);
        sequence++;

        return like;
    }

    public Optional<Like> findByPostIdAndUserId(Long postId, Long userId) {
        return store.values().stream()
                .filter(like -> like.getPostId().equals(postId))
                .filter(like -> like.getUserId().equals(userId))
                .findFirst();
    }

    public boolean existsByPostIdAndUserId(Long postId, Long userId) {
        return findByPostIdAndUserId(postId, userId).isPresent();
    }

    public void delete(Like like) {
        store.remove(like.getId());
    }

    public Long countByPostId(Long postId) {
        return store.values().stream()
                .filter(like -> like.getPostId().equals(postId))
                .count();
    }
}
