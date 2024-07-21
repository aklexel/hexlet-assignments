package exercise.controller;

import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import exercise.model.Comment;
import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public List<PostDTO> index() {
        var posts = postRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return posts;
    }

    @GetMapping(path = "/{id}")
    public PostDTO show(@PathVariable long id) {
        var post = postRepository
                .findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));

        return post;
    }

    private PostDTO toDTO(Post post) {
        var dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());

        var comments = commentRepository
                .findByPostId(post.getId())
                .stream()
                .map(this::toDTO)
                .toList();
        dto.setComments(comments);

        return dto;
    }

    private CommentDTO toDTO(Comment comment) {
        var dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());

        return dto;
    }
}
// END
