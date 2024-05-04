package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentRepository commentRepository;

    //GET /comments — список всех комментариев
    @GetMapping
    public List<Comment> index() {
        return commentRepository.findAll();
    }

    //GET /comments/{id} – просмотр конкретного комментария
    @GetMapping("/{id}")
    public Comment show(@PathVariable long id) {
        var comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Comment with id %d not found", id)));

        return comment;
    }

    //POST /comments – создание нового комментария. При успешном создании возвращается статус 201
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Comment comment) {
        commentRepository.save(comment);
    }

    //PUT /comments/{id} – обновление комментария
    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody Comment comment) {
        commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Comment with id %d not found", id)));

        comment.setId(id);
        commentRepository.save(comment);
    }

    //DELETE /comments/{id} – удаление комментария
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        commentRepository.deleteById(id);
    }

}
// END
