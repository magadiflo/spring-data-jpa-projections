package dev.magadiflo.projections.app.web;

import dev.magadiflo.projections.app.persistence.projections.associations.PostDTO;
import dev.magadiflo.projections.app.persistence.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostRestController {

    private final PostRepository postRepository;

    @GetMapping(path = "/dto-with-to-many-associations")
    public ResponseEntity<List<PostDTO>> getPostDTOByTitle(@RequestParam String postTitle) {
        List<PostDTO> postDTOList = this.postRepository.findPostDTOByTitle(postTitle);
        return ResponseEntity.ok(postDTOList);
    }
}
