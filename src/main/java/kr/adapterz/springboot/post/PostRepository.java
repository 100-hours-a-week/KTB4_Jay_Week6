package kr.adapterz.springboot.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // fetch join을 사용하지 않고 일반 조회
    List<Post> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    //@Query를 활용해서 join fetch 사용
    @Query("""
        select p
        from Post p
        join fetch p.author
        where p.deletedAt is null
        order by p.createdAt desc
    """)
    List<Post> findAllWithAuthorFetchJoin();

    @Query("""
        select p
        from Post p
        join fetch p.author
        where p.id = :postId
    """)
    Optional<Post> findByIdWithAuthor(@Param("postId") Long postId);



}
