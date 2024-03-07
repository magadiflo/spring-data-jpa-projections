package dev.magadiflo.projections.app.persistence.repository.impl;

import dev.magadiflo.projections.app.persistence.projections.associations.PostDTO;
import dev.magadiflo.projections.app.persistence.projections.associations.PostDTOTupleTransformer;
import dev.magadiflo.projections.app.persistence.repository.CustomPostRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked") // Para que no salga el warning al usar el getResultList()
    public List<PostDTO> findPostDTOByTitle(String postTitle) {
        PostDTOTupleTransformer postDTOTupleTransformer = new PostDTOTupleTransformer();

        return this.entityManager.createNativeQuery("""
                        SELECT p.id AS p_id,
                                p.title AS p_title,
                                pc.id AS pc_id,
                                pc.review AS pc_review
                        FROM posts AS p
                            INNER JOIN post_comments AS pc ON(p.id = pc.post_id)
                        WHERE p.title = :postTitle
                        ORDER BY pc.id
                        """)
                .setParameter("postTitle", postTitle)
                .unwrap(Query.class)
                .setTupleTransformer(postDTOTupleTransformer)
                .setResultListTransformer(postDTOTupleTransformer)
                .getResultList();
    }
}
