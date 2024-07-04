package sparta.code3line.domain.like.repository;

import org.springframework.data.repository.CrudRepository;
import sparta.code3line.domain.like.entity.LikeComment;

import java.util.Optional;

public interface LikeCommentRepository extends CrudRepository<LikeComment, Long>, LikeRepositoryCustom {

    Optional<LikeComment> findByUserIdAndCommentId(Long userId, Long commentId);

    Long countByCommentId(Long id);

    Long countByUserId(Long id);

//    @Query("SELECT lc.comment.id FROM LikeComment lc WHERE lc.user.id = :userId")
//    List<Long> findByCommentIdsByUserId(Long userId);



}
