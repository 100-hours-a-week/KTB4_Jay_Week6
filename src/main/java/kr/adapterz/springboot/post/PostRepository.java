package kr.adapterz.springboot.post;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepository {
    private final Map<Long, Post> store = new HashMap<>();
    private Long sequence = 1L;

    public Post save(Post post){
        post.setId(sequence);
        store.put(sequence, post);
        sequence ++;

        return post;
    }
    public List<Post> findAll(){
        return new ArrayList<>(store.values());
    }
    public Optional<Post> findById(Long postId){
        return Optional.ofNullable(store.get(postId));
    }
}
