package kr.adapterz.springboot.comment;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CommentRepository {
    private final Map<Long, Comment> store = new HashMap<>();
    private Long sequence = 1L;

    public Comment save(Comment comment) {
        comment.setId(sequence);
        store.put(sequence, comment);
        sequence++;

        return comment;
    }

    public Optional<Comment> findById(Long commentId) {
        return Optional.ofNullable(store.get(commentId));
    }

    public List<Comment> findByPostId(Long postId) {
        return store.values().stream()
                .filter(comment -> comment.getPostId().equals(postId))
                .toList();
    }

    public List<Comment> findParentCommentsByPostId(Long postId) {
        return store.values().stream()
                .filter(comment -> comment.getPostId().equals(postId))
                .filter(comment -> comment.getParentCommentId() == null)
                .toList();
    }

    public List<Comment> findRepliesByParentCommentId(Long parentCommentId) {
        return store.values().stream()
                .filter(comment -> parentCommentId.equals(comment.getParentCommentId()))
                .toList();
    }

    public Long countByPostId(Long postId) {
        return store.values().stream()
                .filter(comment -> comment.getPostId().equals(postId))
                .filter(comment -> comment.getParentCommentId() == null)
                .filter(comment -> !comment.isDeleted())
                .count();
    }
}
