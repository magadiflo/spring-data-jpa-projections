package dev.magadiflo.projections.app.web;

import dev.magadiflo.projections.app.persistence.projections.IPostCommentSummary;
import dev.magadiflo.projections.app.persistence.projections.PostCommentDTO;
import dev.magadiflo.projections.app.persistence.projections.PostCommentRecord;
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

    @GetMapping(path = "/record-dto-projection")
    public ResponseEntity<Map<String, Object>> getCommentRecordByTitle(@RequestParam String postTitle) {
        List<PostCommentRecord> postCommentRecords = this.postCommentRepository.findCommentRecordByTitle(postTitle);
        HashMap<String, Object> response = new HashMap<>();

        if (!postCommentRecords.isEmpty()) {
            PostCommentRecord recordFirst = postCommentRecords.getFirst();

            //-------------- Ejemplo para verificar igualdad --------
            PostCommentRecord recordCompare = new PostCommentRecord(2L, "Revolution Angular 17", "Se vienen nuevos cambios");
            log.info("¿Record obtenido de la BD es igual al record creado en código?: {}", recordFirst.equals(recordCompare));
            //--------------

            Long postId = recordFirst.id();
            String title = recordFirst.title();

            List<String> reviewList = new ArrayList<>();
            postCommentRecords.forEach(record -> {
                String review = record.review();
                reviewList.add(review);
            });

            response.put("id", postId);
            response.put("title", title);
            response.put("reviews", reviewList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/pojo-dto-projection")
    public ResponseEntity<Map<String, Object>> getCommentDTOByTitle(@RequestParam String postTitle) {
        List<PostCommentDTO> postCommentDTOS = this.postCommentRepository.findCommentDTOByTitle(postTitle);
        HashMap<String, Object> response = new HashMap<>();

        if (!postCommentDTOS.isEmpty()) {
            PostCommentDTO dtoFirst = postCommentDTOS.getFirst();

            //-------------- Ejemplo para verificar igualdad --------
            PostCommentDTO dtoCompare = new PostCommentDTO(2L, "Revolution Angular 17", "Se vienen nuevos cambios");
            log.info("¿POJO DTO obtenido de la BD es igual al POJO DTO creado en código?: {}", dtoFirst.equals(dtoCompare));
            //--------------

            Long postId = dtoFirst.getId();
            String title = dtoFirst.getTitle();

            List<String> reviewList = new ArrayList<>();
            postCommentDTOS.forEach(dto -> {
                String review = dto.getReview();
                reviewList.add(review);
            });

            response.put("id", postId);
            response.put("title", title);
            response.put("reviews", reviewList);
        }
        return ResponseEntity.ok(response);
    }
}
