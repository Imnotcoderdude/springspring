package sparta.code3line.domain.like.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sparta.code3line.domain.like.entity.QLikeBoard;

import java.util.List;

public class LikeBoardRepositoryCustomImpl implements LikeBoardRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public LikeBoardRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Long> findBoardIdsByUserId(Long userId) {
        QLikeBoard likeBoard = QLikeBoard.likeBoard;

        return queryFactory
                .select(likeBoard.board.id)
                .from(likeBoard)
                .where(likeBoard.user.id.eq(userId))
                .fetch();
    }

}
