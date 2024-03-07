package dev.magadiflo.projections.app.persistence.projections.associations;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class PostDTO {

    private final Long id;
    private final String title;
    private final List<PostCommentDTO> comments = new ArrayList<>();

    public PostDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
