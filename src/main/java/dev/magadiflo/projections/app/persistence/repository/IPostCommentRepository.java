package dev.magadiflo.projections.app.persistence.repository;

import dev.magadiflo.projections.app.persistence.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostCommentRepository extends JpaRepository<PostComment, Long> {
}
