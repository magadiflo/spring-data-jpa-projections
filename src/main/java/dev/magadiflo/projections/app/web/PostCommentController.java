package dev.magadiflo.projections.app.web;

import dev.magadiflo.projections.app.persistence.projections.IPostCommentSummary;
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
    public ResponseEntity<Map<String, Object>> getPostWithCommentByTitle(@RequestParam String postTitle) {
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

    @GetMapping(path = "/interface-based-projection")
    public ResponseEntity<Map<String, Object>> getCommentSummaryByTitle(@RequestParam String postTitle) {
        List<IPostCommentSummary> postCommentSummaries = this.postCommentRepository.findCommentSummaryByTitle(postTitle);
        HashMap<String, Object> response = new HashMap<>();

        if (!postCommentSummaries.isEmpty()) {
            IPostCommentSummary projectionFirst = postCommentSummaries.getFirst();

            Long postId = projectionFirst.getId();
            String title = projectionFirst.getTitle();

            List<String> reviewList = new ArrayList<>();
            postCommentSummaries.forEach(projection -> {
                String review = projection.getReview();
                reviewList.add(review);
            });

            response.put("id", postId);
            response.put("title", title);
            response.put("reviews", reviewList);
        }
        return ResponseEntity.ok(response);
    }

}
