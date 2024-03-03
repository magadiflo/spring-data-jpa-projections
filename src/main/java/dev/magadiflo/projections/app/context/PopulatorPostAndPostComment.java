package dev.magadiflo.projections.app.context;

import dev.magadiflo.projections.app.persistence.entity.Post;
import dev.magadiflo.projections.app.persistence.entity.PostComment;
import dev.magadiflo.projections.app.persistence.repository.IPostCommentRepository;
import dev.magadiflo.projections.app.persistence.repository.IPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
@Component
public class PopulatorPostAndPostComment implements InitializingBean {

    private final IPostRepository postRepository;
    private final IPostCommentRepository postCommentRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        Post post1 = new Post();
        post1.setTitle("Tutorial Spring Data JPA");
        post1.setCreatedOn(LocalDate.parse("2024-02-18"));
        post1.setCreatedBy("Martín");
        post1.setUpdatedOn(LocalDate.now());
        post1.setUpdatedBy("Martín");
        post1.setVersion(2);

        Post post2 = new Post();
        post2.setTitle("Revolución de Angular 17");
        post2.setCreatedOn(LocalDate.parse("2024-01-20"));
        post2.setCreatedBy("Martín");
        post2.setUpdatedOn(LocalDate.parse("2024-02-15"));
        post2.setUpdatedBy("Raúl");
        post2.setVersion(3);

        Post post3 = new Post();
        post3.setTitle("Spring Boot 3 y java 21");
        post3.setCreatedOn(LocalDate.parse("2024-03-01"));
        post3.setCreatedBy("Carla");
        post3.setVersion(1);

        this.postRepository.save(post1);
        this.postRepository.save(post2);
        this.postRepository.save(post3);

        PostComment postComment1 = new PostComment();
        postComment1.setReview("¡Excelente!");
        postComment1.setPost(post1);

        PostComment postComment2 = new PostComment();
        postComment2.setReview("Se vienen nuevos cambios");
        postComment2.setPost(post2);

        PostComment postComment3 = new PostComment();
        postComment3.setReview("La nueva sintaxis parece se ve más entendible");
        postComment3.setPost(post2);

        this.postCommentRepository.save(postComment1);
        this.postCommentRepository.save(postComment2);
        this.postCommentRepository.save(postComment3);
    }
}
