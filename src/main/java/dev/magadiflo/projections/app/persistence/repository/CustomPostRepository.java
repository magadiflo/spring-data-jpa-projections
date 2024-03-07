package dev.magadiflo.projections.app.persistence.repository;

import dev.magadiflo.projections.app.persistence.projections.associations.PostDTO;

import java.util.List;

public interface CustomPostRepository {
    List<PostDTO> findPostDTOByTitle(String postTitle);
}
