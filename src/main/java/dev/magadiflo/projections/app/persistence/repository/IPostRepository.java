package dev.magadiflo.projections.app.persistence.repository;

import dev.magadiflo.projections.app.persistence.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostRepository extends JpaRepository<Post, Long> {
}
