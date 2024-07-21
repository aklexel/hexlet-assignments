package exercise.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Getter
@Setter
public class PostDTO {
    public long id;

    public String title;

    public String body;

    public List<CommentDTO> comments;
}
// END
