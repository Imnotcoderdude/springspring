package sparta.code3line.domain.like.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import sparta.code3line.domain.like.entity.LikeBoard;

import java.util.List;
import java.util.Optional;

public interface LikeBoardRepository extends CrudRepository<LikeBoard, Long> {

    Optional<LikeBoard> findByUserIdAndBoardId(Long userId, Long commentId);

    Long countByBoardId(Long boardId);

    // 쿼리문 풀이 :
    // lb 는 LikeBoard 엔티티의 별칭 : FROM LikeBoard lb
    // -> LikeBoard 엔티티와 연관된 Board 엔티티의 id 값을 조회. : SELECT lb.board.id
    // -> 조건 지정을 위해 WHERE 문을 사용하고 조건은 lb 안에 user 필드에서 다른 엔티티인 User 와 연관이 있는 id 값을 나타냄
    // 정리하면 LikeBoard 엔티티를 기준으로 연관이 있는 데이터를 찾기 위해 LikeBoard 엔티티에 저장된 boardId와 userId를 사용하여
    // UserId에 해당하는 사용자가 남긴 좋아요가 달려있는 모든 게시글을 추적한다.
    @Query("SELECT lb.board.id FROM LikeBoard lb WHERE lb.user.id = :userId")
    List<Long> findByBoardIdsByUserId(@Param("userId") Long boardId);

    Long countByUserId(Long id);

}
