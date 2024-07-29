package exercise.controller;

import exercise.dto.AuthorDTO;
import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

    @Autowired
    private AuthorService authorService;

    // BEGIN
    //GET /authors – просмотр списка всех авторов
    @GetMapping
    ResponseEntity<List<AuthorDTO>> index() {
        var authors = authorService.getAll();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(authors.size()))
                .body(authors);
    }

    //GET /authors/{id} – просмотр конкретного автора
    @GetMapping("/{id}")
    AuthorDTO show(@PathVariable Long id) {
        return authorService.get(id);
    }

    //POST /authors – добавление нового автора
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    AuthorDTO create(@RequestBody @Valid AuthorCreateDTO data) {
        return authorService.create(data);
    }

    //PUT /authors/{id} – редактирование автора. При редактировании мы должны иметь возможность поменять имя и фамилию
    @PutMapping("/{id}")
    AuthorDTO update(@PathVariable Long id, @RequestBody @Valid AuthorUpdateDTO data) {
        return authorService.update(id, data);
    }

    //DELETE /authors – удаление автора
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
    // END
}
