package dev.magadiflo.projections.app.persistence.repository.impl;

import dev.magadiflo.projections.app.persistence.projections.associations.PostCommentDTO;
import dev.magadiflo.projections.app.persistence.projections.associations.PostDTO;
import dev.magadiflo.projections.app.persistence.projections.associations.PostDTOTupleTransformer;
import dev.magadiflo.projections.app.persistence.repository.CustomPostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

    @Override
    @SuppressWarnings("unchecked") // Para que no salga el warning en el resultStream
    public List<PostDTO> findPostDTOByTitleProgrammatically(String postTitleParam) {
        Stream<Tuple> resultStream = this.entityManager.createNativeQuery("""
                        SELECT p.id AS p_id,
                                p.title AS p_title,
                                pc.id AS pc_id,
                                pc.review AS pc_review
                        FROM posts AS p
                            INNER JOIN post_comments AS pc ON(p.id = pc.post_id)
                        WHERE p.title = :postTitle
                        ORDER BY pc.id
                        """, Tuple.class)
                .setParameter("postTitle", postTitleParam)
                .getResultStream(); //Ejecute una consulta SELECT y devuelva los resultados de la consulta como java.util.stream.Stream sin tipo.

        Map<Long, PostDTO> postDTOMap = new LinkedHashMap<>();

        resultStream.forEach(tuple -> {
            Long postId = tuple.get("p_id", Long.class);
            String postTitle = tuple.get("p_title", String.class);
            Long postCommentId = tuple.get("pc_id", Long.class);
            String postCommentReview = tuple.get("pc_review", String.class);

            PostDTO currentPostDTO = postDTOMap.computeIfAbsent(postId, pId -> new PostDTO(pId, postTitle));

            PostCommentDTO postCommentDTO = new PostCommentDTO(postCommentId, postCommentReview);
            currentPostDTO.getComments().add(postCommentDTO);
        });

        return new ArrayList<>(postDTOMap.values());
    }
}
