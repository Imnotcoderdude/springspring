package sparta.code3line.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparta.code3line.domain.comment.entity.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findAllByBoardId(Long boardId);

    Page<Comment> findByIdIn(List<Long> likeCommentIds, Pageable pageable);

    Long countByBoardId(Long id);
}
