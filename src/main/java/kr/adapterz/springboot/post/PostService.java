package kr.adapterz.springboot.post;

import kr.adapterz.springboot.comment.Comment;
import kr.adapterz.springboot.comment.CommentRepository;
import kr.adapterz.springboot.comment.dto.CommentDetailResponse;
import kr.adapterz.springboot.comment.dto.ReplyCreateResponse;
import kr.adapterz.springboot.global.exception.BadRequestException;
import kr.adapterz.springboot.global.exception.ForbiddenException;
import kr.adapterz.springboot.global.exception.NotFoundException;
import kr.adapterz.springboot.like.LikeRepository;
import kr.adapterz.springboot.post.dto.*;
import kr.adapterz.springboot.user.User;
import kr.adapterz.springboot.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PostViewRepository postViewRepository;

    public PostResponse post(PostRequest request){
        if (request.getTitle() == null || request.getTitle().isBlank()){
            throw new BadRequestException("empty_title");
        }
        if (request.getContent() == null || request.getContent().isBlank()){
            throw new BadRequestException("empty_content");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("user_not_found"));

        if (user.isDeleted()) {
            throw new BadRequestException("deleted_user");
        }
        Post post = new Post(
                user,
                request.getTitle(),
                request.getContent()
        );

        Post savedPost = postRepository.save(post);
        return new PostResponse(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                user.getNickname(),
                savedPost.getCreatedAt()
        );
    }
    public List<PostListResponse> getPost() {
        List<Post> posts = postRepository.findAllWithAuthorFetchJoin();

        return posts.stream()
                .filter(post -> !post.isDeleted())
                .map(post -> {
                    User author = userRepository.findById(post.getAuthor().getId())
                            .orElseThrow(() -> new NotFoundException("user_not_found"));
                    if (post.isBlinded()) {
                        return PostListResponse.builder()
                                .postId(post.getId())
                                .title("블라인드 처리된 게시글입니다.")
                                .authorNickname("블라인드 처리된 사용자입니다.")
                                .commentCount(null)
                                .viewCount(null)
                                .createdAt(post.getCreatedAt())
                                .updatedAt(null)
                                .authorDeleted(author.isDeleted())
                                .blinded(true)
                                .build();
                    }
                    String authorNickname = author.isDeleted()
                            ? "알 수 없음"
                            : author.getNickname();
                    return PostListResponse.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .authorNickname(authorNickname)
                            .commentCount((long)post.getCommentCount())
                            .viewCount((long) post.getViewCount())
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .authorDeleted(author.isDeleted())
                            .blinded(false)
                            .build();
                })
                .toList();
    }
    // 게시글 상세 조회
    public PostDetailResponse getPostDetail(Long postId, Long userId){
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> new NotFoundException("post_not_found"));
        if (post.isDeleted()){
            throw new NotFoundException("post_not_found");
        }
        User author = userRepository.findById(post.getAuthor().getId())
                .orElseThrow(() -> new NotFoundException("user_not_found"));

        if (!post.isBlinded()) {
            increaseViewCount(post, userId);
        }

        if (post.isBlinded()){
            return PostDetailResponse.builder()
                    .postId(post.getId())
                    .title("블라인드 처리된 게시글입니다.")
                    .content(null)
                    .authorNickname("블라인드 처리된 사용자입니다.")
                    .authorProfileImage(null)
                    .authorDeleted(author.isDeleted())
                    .blinded(true)
                    .createdAt(post.getCreatedAt())
                    .updatedAt(null)
                    .likeCount(null)
                    .viewCount(null)
                    .commentCount(null)
                    .comments(List.of())
                    .build();
        }

        String authorNickname = author.isDeleted()
                ? "알 수 없음"
                : author.getNickname();

        return PostDetailResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorNickname(authorNickname)
                .authorProfileImage(author.getProfileImage())
                .authorDeleted(author.isDeleted())
                .blinded(false)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .likeCount(likeRepository.countByPost_Id(postId))
                .viewCount((long) post.getViewCount())
                .commentCount(commentRepository.countByPostId(postId))
                .comments(getComments(postId))
                .build();
    }
    public void deletePost(Long postId, DeletePostRequest request){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("post_not_found"));
        if (post.isDeleted()){
            throw new NotFoundException("post_not_found");
        }
        validateAuthor(post, request.getUserId());

        post.delete();
    }
    public UpdatePostResponse updatePost(Long postId,UpdatePostRequest request){
        if (request.getTitle() == null || request.getTitle().isBlank()){
            throw new BadRequestException("empty_title");
        }
        if (request.getContent() == null || request.getContent().isBlank()){
            throw new BadRequestException("empty_content");
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new NotFoundException("post_not_found"));
        if (post.isDeleted()){
            throw new NotFoundException("post_not_found");
        }
        User user = userRepository.findById(post.getAuthor().getId())
                .orElseThrow(()->new NotFoundException("user_not_found"));
        validateAuthor(post, request.getUserId());
        post.update(request.getTitle(), request.getContent());

        return new UpdatePostResponse(
                postId,
                request.getTitle(),
                request.getContent(),
                user.getNickname(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    // 상세 조회에 들어갈 댓글 목록 조회
    private List<CommentDetailResponse> getComments(Long postId) {
        return commentRepository.findParentCommentsByPost_Id(postId).stream()
                .map(comment -> {
                    User author = userRepository.findById(comment.getAuthor().getId())
                            .orElseThrow(() -> new NotFoundException("user_not_found"));
                    return new CommentDetailResponse(
                            comment.getId(),
                            comment.isDeleted() ? "삭제된 댓글입니다." : comment.getContent(),
                            author.isDeleted() ? "알 수 없음" : author.getNickname(),
                            author.isDeleted(),
                            comment.isDeleted(),
                            comment.getCreatedAt(),
                            comment.getUpdatedAt(),
                            getReplies(comment)
                    );
                })
                .toList();
    }

    // 댓글에 달린 대댓글 목록 조회
    private List<ReplyCreateResponse> getReplies(Comment parentComment) {
        return commentRepository.findRepliesByParentComment_Id(parentComment.getId()).stream()
                .filter(reply -> !reply.isDeleted())
                .map(reply -> {
                    User author = userRepository.findById(reply.getAuthor().getId())
                            .orElseThrow(() -> new NotFoundException("user_not_found"));
                    return new ReplyCreateResponse(
                            reply.getId(),
                            parentComment.getId(),
                            reply.getContent(),
                            author.isDeleted() ? "알 수 없음" : author.getNickname(),
                            author.isDeleted(),
                            reply.getCreatedAt()
                    );
                })
                .toList();
    }
    private void validateAuthor(Post post, Long userId) {
        if (!post.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException();
        }
    }

    private void increaseViewCount(Post post, Long userId) {
        if (userId == null) {
            throw new BadRequestException("empty_user_id");
        }
        // 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user_not_found"));

        //현재 시간 now에 저장
        LocalDateTime now = LocalDateTime.now();

        //optional 상자에 postView 담기 있을 수도 있고 없을 수도 있고...
        Optional<PostView> optionalPostView
                = postViewRepository.findByPost_IdAndUser_Id(post.getId(),userId);

        // 만약 상자 안에 진짜 postView 객체가 있으면?
        // 조회한지 24시간 지났는지 먼저 boolean 타입으로 체크
        if (optionalPostView.isPresent()){
            // 상자에서 진짜 postView 먼저 꺼내기
            PostView postView = optionalPostView.get();

            //24시간 지났으면 false 아니면 true
            boolean alreadyViewed = postView.getViewedAt().plusHours(24).isAfter(now);
            // 24시간 안 지났으면 return
            if (alreadyViewed){
                return;
            }
            //24시간 지났으면 조회수 올리고 viewedAt 업데이트하고 끝
            // 어차피 업데이트만 해도 더티체킹으로 Update 가능
            post.increaseViewCount();
            postView.updatedViewedAt(now);
            return;
        }

        // 상자에 진짜 postView가 없다면 viewcount를 1 증가시키고 저장

        post.increaseViewCount();
        postViewRepository.save(new PostView(post, user, now));
    }
}
