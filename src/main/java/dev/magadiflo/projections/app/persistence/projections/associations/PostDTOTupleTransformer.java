package dev.magadiflo.projections.app.persistence.projections.associations;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.ResultListTransformer;
import org.hibernate.query.TupleTransformer;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class PostDTOTupleTransformer implements TupleTransformer<PostDTO>, ResultListTransformer<PostDTO> {

    private static final String POST_ID = "p_id";
    private static final String POST_TITLE = "p_title";
    private static final String POST_COMMENT_ID = "pc_id";
    private static final String POST_COMMENT_REVIEW = "pc_review";
    private final Map<Long, PostDTO> postDTOMap = new LinkedHashMap<>();

    @Override
    public PostDTO transformTuple(Object[] tuple, String[] aliases) {
        List<String> aliasList = Arrays.asList(aliases);
        Map<String, Object> rowMap = aliasList.stream().collect(Collectors.toMap(alias -> alias, alias -> tuple[aliasList.indexOf(alias)]));

        Long postId = (Long) rowMap.get(POST_ID);
        String postTitle = (String) rowMap.get(POST_TITLE);
        Long postCommentId = (Long) rowMap.get(POST_COMMENT_ID);
        String postCommentReview = (String) rowMap.get(POST_COMMENT_REVIEW);

        PostDTO currentPostDTO = this.postDTOMap.computeIfAbsent(postId, pId -> new PostDTO(pId, postTitle));

        PostCommentDTO postCommentDTO = new PostCommentDTO(postCommentId, postCommentReview);
        currentPostDTO.getComments().add(postCommentDTO);

        return currentPostDTO;
    }

    @Override
    public List<PostDTO> transformList(List<PostDTO> resultList) {
        log.info("r: {}", resultList);
        log.info("m. {}", postDTOMap.values());
        return new ArrayList<>(postDTOMap.values());
    }

}
