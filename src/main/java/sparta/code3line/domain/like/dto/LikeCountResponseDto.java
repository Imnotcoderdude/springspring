package sparta.code3line.domain.like.dto;

import lombok.Data;

@Data
public class LikeCountResponseDto {

    private Long boardId;
    private Long likeCount;

    public LikeCountResponseDto(Long boardId, Long likeCount) {
        this.boardId = boardId;
        this.likeCount = likeCount;
    }
}
