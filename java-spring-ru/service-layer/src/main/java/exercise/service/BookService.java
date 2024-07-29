package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    BookRepository repository;

    @Autowired
    BookMapper bookMapper;

    public List<BookDTO> getAll() {
        return repository
                .findAll()
                .stream()
                .map(bookMapper::map)
                .toList();
    }

    public BookDTO get(Long id) {
        var book = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id = " + id));

        return bookMapper.map(book);
    }

    public BookDTO create(BookCreateDTO dto) {
        var book = bookMapper.map(dto);
        repository.save(book);
        return bookMapper.map(book);
    }

    public BookDTO update(Long id, BookUpdateDTO dto) {
        var book = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id = " + id));

        bookMapper.update(dto, book);
        repository.save(book);
        return bookMapper.map(book);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    // END
}
