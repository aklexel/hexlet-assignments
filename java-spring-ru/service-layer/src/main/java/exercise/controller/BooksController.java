package exercise.controller;

import java.util.List;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BookService bookService;

    // BEGIN
    //GET /books – просмотр списка всех книг
    @GetMapping
    ResponseEntity<List<BookDTO>> index() {
        var books = bookService.getAll();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(books.size()))
                .body(books);
    }

    //GET /books/{id} – просмотр конкретной книги
    @GetMapping("/{id}")
    BookDTO show(@PathVariable Long id) {
        return bookService.get(id);
    }

    //POST /books – добавление новой книги. При указании id несуществующего автора должен вернуться ответ с кодом 400 Bad request
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BookDTO create(@RequestBody @Valid BookCreateDTO data) {
        return bookService.create(data);
    }

    //PUT /books/{id} – редактирование книги. При редактировании мы должны иметь возможность поменять название и автора. При указании id несуществующего автора также должен вернуться ответ с кодом 400 Bad request
    @PutMapping("/{id}")
    BookDTO update(@PathVariable Long id, @RequestBody @Valid BookUpdateDTO data) {
        return bookService.update(id, data);
    }

    //DELETE /books/{id} – удаление книги
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
    // END
}
