package dev.magadiflo.projections.app.web;

import dev.magadiflo.projections.app.persistence.repository.IPostCommentRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/post-comments")
public class PostCommentController {

    private final IPostCommentRepository postCommentRepository;

    @GetMapping(path = "/tuples")
    public ResponseEntity<Map<String, Object>> searchEmployees(@RequestParam String postTitle) {
        List<Tuple> postWithPostComments = this.postCommentRepository.findCommentTupleByTitle(postTitle);
        HashMap<String, Object> response = new HashMap<>();

        if (!postWithPostComments.isEmpty()) {
            Tuple postTuple = postWithPostComments.getFirst();

            Long postId = postTuple.get("id", Long.class);
            String title = postTuple.get("title", String.class);

            List<String> reviewList = new ArrayList<>();
            postWithPostComments.forEach(tuple -> {
                String review = tuple.get("review", String.class);
                reviewList.add(review);
            });

            response.put("id", postId);
            response.put("title", title);
            response.put("reviews", reviewList);
        }

        return ResponseEntity.ok(response);
    }

}
