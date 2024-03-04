package dev.magadiflo.projections.app.persistence.repository;

import dev.magadiflo.projections.app.persistence.entity.PostComment;
import dev.magadiflo.projections.app.persistence.projections.IPostCommentSummary;
import dev.magadiflo.projections.app.persistence.projections.PostCommentDTO;
import dev.magadiflo.projections.app.persistence.projections.PostCommentRecord;
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

    @Query("""
            SELECT new dev.magadiflo.projections.app.persistence.projections.PostCommentRecord(p.id AS id,
                                        p.title AS title,
                                        pc.review AS review)
            FROM PostComment AS pc
                JOIN pc.post AS p
            WHERE p.title LIKE :postTitle
            ORDER BY pc.id
            """)
    List<PostCommentRecord> findCommentRecordByTitle(@Param("postTitle") String postTitle);

    @Query("""
            SELECT new dev.magadiflo.projections.app.persistence.projections.PostCommentDTO(p.id AS id,
                                        p.title AS title,
                                        pc.review AS review)
            FROM PostComment AS pc
                JOIN pc.post AS p
            WHERE p.title LIKE :postTitle
            ORDER BY pc.id
            """)
    List<PostCommentDTO> findCommentDTOByTitle(@Param("postTitle") String postTitle);
}
