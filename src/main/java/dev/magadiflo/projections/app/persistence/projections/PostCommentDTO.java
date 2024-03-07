package dev.magadiflo.projections.app.persistence.projections;

import java.util.Objects;

public class PostCommentDTO {
    private final Long id;
    private final String title;
    private final String review;

    public PostCommentDTO(Long id, String title, String review) {
        this.id = id;
        this.title = title;
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReview() {
        return review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostCommentDTO that = (PostCommentDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(review, that.review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, review);
    }
}
