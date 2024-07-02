package sparta.code3line.domain.like.repository;

import org.springframework.data.repository.CrudRepository;
import sparta.code3line.domain.like.entity.LikeBoard;

import java.util.Optional;

public interface LikeBoardRepository extends CrudRepository<LikeBoard, Long> {

    Optional<LikeBoard> findByUserIdAndBoardId(Long userId, Long commentId);

    Long countByBoardId(Long boardId);

}
