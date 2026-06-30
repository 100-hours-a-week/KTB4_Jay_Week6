package kr.adapterz.springboot.postDraft;

import jakarta.validation.Valid;
import kr.adapterz.springboot.global.ApiResponse;
import kr.adapterz.springboot.post.dto.PostResponse;
import kr.adapterz.springboot.postDraft.dto.DraftResponse;
import kr.adapterz.springboot.postDraft.dto.DraftSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/draft")
public class PostDraftController {

    private final PostDraftService postDraftService;

    @PostMapping
    public ResponseEntity<ApiResponse<DraftResponse>> saveDraft(
            @Valid @RequestBody DraftSaveRequest request
    ) {
        DraftResponse response = postDraftService.saveDraft(request);

        return ResponseEntity.ok(new ApiResponse<>("draft_save_success", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DraftResponse>> getDraft(
            @RequestParam Long userId
    ) {
        DraftResponse response = postDraftService.getDraft(userId);

        return ResponseEntity.ok(new ApiResponse<>("draft_get_success", response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteDraft(
            @RequestParam Long userId
    ) {
        postDraftService.deleteDraft(userId);

        return ResponseEntity.ok(new ApiResponse<>("draft_delete_success", null));
    }

    @PostMapping("/publish")
    public ResponseEntity<ApiResponse<PostResponse>> publishDraft(
            @RequestParam Long userId
    ) {
        PostResponse response = postDraftService.publishDraft(userId);

        return ResponseEntity.ok(new ApiResponse<>("draft_publish_success", response));
    }
}
