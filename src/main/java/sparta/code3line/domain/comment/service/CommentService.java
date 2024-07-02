package sparta.code3line.domain.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sparta.code3line.common.CommonResponse;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;
import sparta.code3line.domain.board.dto.BoardResponseDto;
import sparta.code3line.domain.board.entity.Board;
import sparta.code3line.domain.board.repository.BoardRepository;
import sparta.code3line.domain.comment.dto.CommentRequestDto;
import sparta.code3line.domain.comment.dto.CommentResponseDto;
import sparta.code3line.domain.comment.entity.Comment;
import sparta.code3line.domain.comment.repository.CommentRepository;
import sparta.code3line.domain.like.repository.LikeBoardRepository;
import sparta.code3line.domain.like.repository.LikeCommentRepository;
import sparta.code3line.domain.user.entity.User;
import sparta.code3line.security.UserPrincipal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeCommentRepository likeCommentRepository;

    public CommentResponseDto createComment(Long boardId, User user, CommentRequestDto requestDto) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );

        Comment comment = Comment.builder()
                .user(user)
                .board(board)
                .contents(requestDto.getContents())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);

    }

    public List<CommentResponseDto> readComments(Long boardId) {

        List<Comment> comments = commentRepository.findAllByBoardId(boardId).orElse(null);
        List<CommentResponseDto> responseDto = new ArrayList<>();

        if(comments != null) {
            for (Comment comment : comments) {
                responseDto.add(new CommentResponseDto(comment));
            }
        }

        return responseDto;

    }

    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long commentId, User user, CommentRequestDto requestDto) {

        Comment comment = getComment(boardId, commentId, user);

        comment.updateContent(requestDto.getContents());

        return new CommentResponseDto(comment);

    }

    @Transactional
    public void deleteComment(Long boardId, Long commentId, User user) {

        Comment comment = getComment(boardId, commentId, user);

        commentRepository.delete(comment);

    }

    // 댓글 단건 조회
    public CommentResponseDto getOneComment(Long boardId, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getBoard().getId().equals(boardId)) {
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        }

        Long commentLikeCount = likeCommentRepository.countByCommentId(comment.getId());
        return new CommentResponseDto(comment, commentLikeCount);
    }

    // comment 찾아오기
    private Comment getComment(Long boardId, Long commentId, User user) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (!comment.getBoard().getId().equals(boardId)) {
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        }

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.USERNAME_NOT_FOUND);
        }

        return comment;

    }

    // 조회 : 사용자가 좋아요한 댓글 전체 조회
    public Page<CommentResponseDto> getAllLikeBoard(int page, int size, UserPrincipal userPrincipal) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Long userId = userPrincipal.getUser().getId();

        List<Long> likeCommentIds = likeCommentRepository.findByCommentIdsByUserId(userId);

        Page<Comment> likeComments = commentRepository.findByIdIn(likeCommentIds, pageable);

        List<CommentResponseDto> commentResponseDtoList = likeComments.getContent().stream()
                .map(comment -> {
                    Long likeCount = likeCommentRepository.countByCommentId(comment.getId());
                    return new CommentResponseDto(comment, likeCount);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(commentResponseDtoList, pageable, likeComments.getTotalElements());
    }
}
