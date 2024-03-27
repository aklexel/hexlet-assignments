package exercise.controller.users;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// END
@RestController
@RequestMapping("/api")
public class PostsController {
    private final List<Post> posts = Data.getPosts();

    // GET /api/users/{id}/posts — список всех постов, написанных пользователем с таким же userId, как id в маршруте
    @GetMapping("/users/{id}/posts")
    public List<Post> posts(@PathVariable Integer id) {
        return posts
                .stream()
                .filter(p -> p.getUserId() == id)
                .toList();
    }

    // POST /api/users/{id}/posts – создание нового поста, привязанного к пользователю по id.
    // Код должен возвращать статус 201, тело запроса должно содержать slug, title и body. Обратите внимание, что userId берется из маршрута
    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@PathVariable Integer id, @RequestBody Post post) {
        post.setUserId(id);
        posts.add(post);
        return post;
    }
}

// BEGIN


