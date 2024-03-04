package dev.magadiflo.projections.app.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate createdOn;
    private String createdBy;
    private LocalDate updatedOn;
    private String updatedBy;
    @Version
    private Integer version;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<PostComment> comments = new ArrayList<>();

    public Post addComment(PostComment comment) {
        this.comments.add(comment);
        comment.setPost(this);
        return this;
    }

    public void deleteComment(PostComment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
    }
}
