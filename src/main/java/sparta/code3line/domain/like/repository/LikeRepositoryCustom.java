package sparta.code3line.domain.like.repository;

import java.util.List;

public interface LikeRepositoryCustom {
    List<Long> findByBoardIdsByUserId(Long userId);

    List<Long> findByCommentIdsByUserId(Long userId);
}
