package sparta.code3line.domain.like.repository;

import java.util.List;

public interface LikeBoardRepositoryCustom {
    List<Long> findBoardIdsByUserId(Long userId);

}
