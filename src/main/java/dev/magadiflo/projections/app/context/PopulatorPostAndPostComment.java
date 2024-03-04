package dev.magadiflo.projections.app.context;

import dev.magadiflo.projections.app.persistence.entity.Post;
import dev.magadiflo.projections.app.persistence.entity.PostComment;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        Post post1 = new Post();
        post1.setTitle("Tutorial Spring Data JPA");
        post1.setCreatedOn(LocalDate.parse("2024-02-18"));
        post1.setCreatedBy("Martín");
        post1.setUpdatedOn(LocalDate.now());
        post1.setUpdatedBy("Martín");

        Post post2 = new Post();
        post2.setTitle("Revolution Angular 17");
        post2.setCreatedOn(LocalDate.parse("2024-01-20"));
        post2.setCreatedBy("Martín");
        post2.setUpdatedOn(LocalDate.parse("2024-02-15"));
        post2.setUpdatedBy("Raúl");

        Post post3 = new Post();
        post3.setTitle("Spring Boot 3 y java 21");
        post3.setCreatedOn(LocalDate.parse("2024-03-01"));
        post3.setCreatedBy("Carla");

        PostComment postComment1 = new PostComment();
        postComment1.setReview("¡Excelente!");

        PostComment postComment2 = new PostComment();
        postComment2.setReview("Se vienen nuevos cambios");

        PostComment postComment3 = new PostComment();
        postComment3.setReview("La nueva sintaxis parece se ve más entendible");

        post1.addComment(postComment1);
        post2.addComment(postComment2).addComment(postComment3);

        log.info("Poblando tabla posts con sus post_comments");
        this.postRepository.save(post1);
        this.postRepository.save(post2);
        this.postRepository.save(post3);
    }
}
