package kr.adapterz.springboot.comment;

import kr.adapterz.springboot.comment.dto.*;
import kr.adapterz.springboot.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request
    ) {
        CommentCreateResponse response = commentService.createComment(postId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("comment_create_success", response));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        CommentUpdateResponse response = commentService.updateComment(commentId, request);

        return ResponseEntity.ok()
                .body(new ApiResponse<>("comment_edit_success", response));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentDeleteResponse>> deleteComment(
            @PathVariable Long commentId,
            @RequestBody CommentDeleteRequest request
    ) {
        CommentDeleteResponse response = commentService.deleteComment(commentId, request);

        return ResponseEntity.ok()
                .body(new ApiResponse<>("comment_delete_success", response));
    }

    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<ApiResponse<ReplyCreateResponse>> createReply(
            @PathVariable Long commentId,
            @RequestBody ReplyCreateRequest request
    ) {
        ReplyCreateResponse response = commentService.createReply(commentId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("reply_created_success", response));
    }

    @PatchMapping("/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<ApiResponse<ReplyCreateResponse>> updateReply(
            @PathVariable Long commentId,
            @PathVariable Long replyId,
            @RequestBody ReplyUpdateRequest request
    ) {
        ReplyCreateResponse response = commentService.updateReply(commentId, replyId, request);

        return ResponseEntity.ok()
                .body(new ApiResponse<>("replyedit_created_success", response));
    }
}
