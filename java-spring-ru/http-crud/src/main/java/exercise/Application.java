package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    //Реализуйте полный CRUD сущности Post по аналогии с тем, как мы делали это в уроке. Необходимо реализовать следующие маршруты:
    //
    //GET /posts — список всех постов
    //GET /posts/{id} — просмотр конкретного поста
    //POST /posts – создание нового поста
    //PUT /posts/{id} – обновление поста
    //DELETE /posts/{id} – удаление поста
    // Бонусное задание: реализуйте вывод списка постов с помощью пейджинга.
    // Номер страницы и количество постов на странице передаются в качестве параметров строки запроса — например, /posts?page=2&limit=10.
    // По умолчанию должна выводиться первая страница.

    @GetMapping("/posts") // список всех постов
    public List<Post> index(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        return posts
                .stream()
                .skip((page - 1) * limit)
                .limit(limit)
                .toList();
    }

    @GetMapping("/posts/{id}") // просмотр конкретного поста
    public Optional<Post> show(@PathVariable String id) {
        var page = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        return page;
    }

    @PostMapping("/posts") // создание нового поста
    public Post create(@RequestBody Post post) {
        posts.add(post);
        return post;
    }

    @PutMapping("/posts/{id}") // обновление поста
    public Post update(@PathVariable String id, @RequestBody Post data) {
        var postOpt = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (postOpt.isPresent()) {
            var post = postOpt.get();
            post.setId(data.getId());
            post.setTitle(data.getTitle());
            post.setBody(data.getBody());
        }
        return data;
    }

    @DeleteMapping("/posts/{id}") // удаление поста
    public void remove(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
    // END
}
