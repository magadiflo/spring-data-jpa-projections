package dev.magadiflo.projections.app.persistence.projections.associations;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PostCommentDTO {

    private final Long id;
    private final String review;

    public PostCommentDTO(Long id, String review) {
        this.id = id;
        this.review = review;
    }
}
