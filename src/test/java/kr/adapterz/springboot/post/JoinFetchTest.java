package kr.adapterz.springboot.post;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.adapterz.springboot.user.User;
import kr.adapterz.springboot.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@Transactional
class JoinFetchTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    EntityManager em;

    //일반 테스트
    @Test
    void fetch_join_with_pageable_does_not_cause_n_plus_one() {
        // given
        for (int i = 1; i <= 5; i++) {
            User user = userRepository.save(new User(
                    "user" + i + "@test.com",
                    "1234",
                    "user" + i,
                    null
            ));

            postRepository.save(new Post(
                    user,
                    "title" + i,
                    "content" + i
            ));
        }

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Post> posts = postRepository.findAllWithAuthorFetchJoin(pageable);

        // then
        for (Post post : posts.getContent()) {
            System.out.println(post.getAuthor().getNickname());
        }
    }

    @Test
    void fetch_join_prevents_n_plus_one() {
        // given
        for (int i = 1; i <= 5; i++) {
            User user = userRepository.save(new User(
                    "fetch" + i + "@test.com",
                    "1234",
                    "fetchUser" + i,
                    null
            ));

            postRepository.save(new Post(
                    user,
                    "title" + i,
                    "content" + i
            ));
        }

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Post> posts = postRepository.findAllWithAuthorFetchJoin(pageable);

        // then
        for (Post post : posts.getContent()) {
            System.out.println(post.getAuthor().getNickname());
        }
    }
}
