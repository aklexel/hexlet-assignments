package exercise.controller;

import exercise.repository.CommentRepository;
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

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    //GET /posts — список всех постов
    @GetMapping
    public List<Post> index() {
        return postRepository.findAll();
    }

    //GET /posts/{id} – просмотр конкретного поста
    @GetMapping(path = "/{id}")
    public Post show(@PathVariable long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Post with id %d not found", id)));
        return post;
    }

    //POST /posts – создание нового поста. При успешном создании возвращается статус 201
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Post post) {
        postRepository.save(post);
    }

    //PUT /posts/{id} – обновление поста
    @PutMapping(path = "/{id}")
    public void update(@PathVariable long id, @RequestBody Post post) {
        postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format("Post with id %d not found", id)));

        post.setId(id);
        postRepository.save(post);
    }

    //DELETE /posts/{id} – удаление поста. При удалении поста удаляются все комментарии этого поста
    //Используйте метод deleteByPostId() в репозитории комментариев для удаления комментариев по id поста.
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        postRepository.deleteById(id);
        commentRepository.deleteByPostId(id);
    }

}
// END
