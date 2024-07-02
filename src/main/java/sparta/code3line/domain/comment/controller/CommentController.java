package sparta.code3line.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.comment.dto.CommentRequestDto;
import sparta.code3line.domain.comment.dto.CommentResponseDto;
import sparta.code3line.domain.comment.service.CommentService;
import sparta.code3line.security.UserPrincipal;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}/comment")
    public ResponseEntity<CommonResponse<CommentResponseDto>> createComment(@PathVariable Long boardId,
                                                                            @AuthenticationPrincipal UserPrincipal principal,
                                                                            @RequestBody CommentRequestDto requestDto) {

        CommonResponse<CommentResponseDto> response = new CommonResponse<>(
                "댓글 생성 완료 🎉",
                HttpStatus.CREATED.value(),
                commentService.createComment(boardId, principal.getUser(), requestDto)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/{boardId}/comments")
    public ResponseEntity<CommonResponse<List<CommentResponseDto>>> readComments(@PathVariable Long boardId) {

        CommonResponse<List<CommentResponseDto>> response = new CommonResponse<>(
                "댓글 전체 조회 완료 🎉",
                HttpStatus.OK.value(),
                commentService.readComments(boardId)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // 조회 : 댓글 단건 조회 + 좋아요 수 출력
    @GetMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> getOneComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId
            ) {

        CommonResponse<CommentResponseDto> response = new CommonResponse<>(
                "댓글 단건 조회 완료 🎉",
                HttpStatus.OK.value(),
                commentService.getOneComment(boardId, commentId)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // 조회 : 사용자가 좋아요한 댓글 전체조회
    @GetMapping("/comments/likes")
    public ResponseEntity<CommonResponse<Page<CommentResponseDto>>> getAllLikeComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "1") int page
    ) {

        int sizeFixed = 5;
        Page<CommentResponseDto> responseDto = commentService.getAllLikeBoard(
                page - 1,
                sizeFixed,
                userPrincipal);
        CommonResponse<Page<CommentResponseDto>> commonResponse = new CommonResponse<>(
                "좋아요한 게시글 " + page + "번 페이지 조회 완료",
                HttpStatus.OK.value(),
                responseDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);

    }

    @PutMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(@PathVariable Long boardId,
                                                                            @PathVariable Long commentId,
                                                                            @AuthenticationPrincipal UserPrincipal principal,
                                                                            @RequestBody CommentRequestDto requestDto) {

        CommonResponse<CommentResponseDto> response = new CommonResponse<>(
                "댓글 수정 완료 🎉",
                HttpStatus.OK.value(),
                commentService.updateComment(boardId, commentId, principal.getUser(), requestDto)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<CommonResponse<Void>> deleteComment(@PathVariable Long boardId,
                                                              @PathVariable Long commentId,
                                                              @AuthenticationPrincipal UserPrincipal principal) {
        commentService.deleteComment(boardId, commentId, principal.getUser());
        CommonResponse<Void> response = new CommonResponse<>(
                "댓글 삭제 완료 🎉",
                HttpStatus.OK.value(),
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
