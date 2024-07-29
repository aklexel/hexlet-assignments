package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    private AuthorRepository repository;

    @Autowired
    private AuthorMapper authorMapper;

    public List<AuthorDTO> getAll() {
        return repository
                .findAll()
                .stream()
                .map(authorMapper::map)
                .toList();
    }

    public AuthorDTO get(Long id) {
        var author = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id = " + id));

        return authorMapper.map(author);
    }

    public AuthorDTO create(AuthorCreateDTO dto) {
        var author = authorMapper.map(dto);
        repository.save(author);

        return authorMapper.map(author);
    }

    public AuthorDTO update(Long id, AuthorUpdateDTO data) {
        var author = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found with id = " + id));

        authorMapper.update(data, author);
        repository.save(author);

        return authorMapper.map(author);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    // END
}
