package sparta.code3line.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sparta.code3line.domain.like.entity.LikeComment;

import java.util.List;
import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

    Optional<LikeComment> findByUserIdAndCommentId(Long userId, Long commentId);

    Long countByCommentId(Long id);

    @Query("SELECT lc.comment.id FROM LikeComment lc WHERE lc.user.id = :userId")
    List<Long> findByCommentIdsByUserId(Long userId);
}
