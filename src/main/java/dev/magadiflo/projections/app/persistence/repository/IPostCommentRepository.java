package dev.magadiflo.projections.app.persistence.repository;

import dev.magadiflo.projections.app.persistence.entity.PostComment;
import dev.magadiflo.projections.app.persistence.projections.IPostCommentSummary;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query("""
            SELECT p.id AS id,
                    p.title AS title,
                    pc.review AS review
            FROM PostComment AS pc
                JOIN pc.post AS p
            WHERE p.title LIKE :postTitle
            ORDER BY pc.id
            """)
    List<Tuple> findCommentTupleByTitle(@Param("postTitle") String postTitle);

    @Query("""
            SELECT p.id AS id,
                    p.title AS title,
                    pc.review AS review
            FROM PostComment AS pc
                JOIN pc.post AS p
            WHERE p.title LIKE :postTitle
            ORDER BY pc.id
            """)
    List<IPostCommentSummary> findCommentSummaryByTitle(@Param("postTitle") String postTitle);
}
