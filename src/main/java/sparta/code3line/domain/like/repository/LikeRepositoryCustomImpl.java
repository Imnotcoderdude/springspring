package sparta.code3line.domain.like.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sparta.code3line.domain.like.entity.QLikeBoard;
import sparta.code3line.domain.like.entity.QLikeComment;

import java.util.List;

public class LikeRepositoryCustomImpl implements LikeRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Long> findByBoardIdsByUserId(Long userId) {
        QLikeBoard likeBoard = QLikeBoard.likeBoard;

        return queryFactory
                .select(likeBoard.board.id)
                .from(likeBoard)
                .where(likeBoard.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Long> findByCommentIdsByUserId(Long userId) {
        QLikeComment likeComment = QLikeComment.likeComment;

        return queryFactory
                .select(likeComment.comment.id)
                .from(likeComment)
                .where(likeComment.user.id.eq(userId))
                .fetch();

    }

}
