package sparta.code3line.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.code3line.domain.like.entity.LikeComment;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

    Optional<LikeComment> findByUserIdAndCommentId(Long userId, Long commentId);

    Long countByCommentId(Long id);
}
