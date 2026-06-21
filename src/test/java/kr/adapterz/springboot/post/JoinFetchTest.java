package kr.adapterz.springboot.post;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.adapterz.springboot.user.User;
import kr.adapterz.springboot.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    void normal_query_causes_n_plus_one() {
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

        // when
        List<Post> posts = postRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();

        // then
        for (Post post : posts) {
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

        // when
        List<Post> posts = postRepository.findAllWithAuthorFetchJoin();

        // then
        for (Post post : posts) {
            System.out.println(post.getAuthor().getNickname());
        }
    }
}
