package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/people")
public class PeopleController {
    @Autowired
    JdbcTemplate jdbc;

    @PostMapping
    public Map<String, Object> createPerson(@RequestBody Map<String, Object> person) {
        String query = "INSERT INTO person (first_name, last_name) VALUES (?, ?)";
        jdbc.update(query, person.get("first_name"), person.get("last_name"));
        return person;
    }

    // BEGIN
    @GetMapping
    public List<Map<String, Object>> getAllPersons() {
        String query = "SELECT id, first_name, last_name FROM person";
        return jdbc.queryForList(query);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getPerson(@PathVariable Integer id) {
        String query = "SELECT id, first_name, last_name FROM person WHERE id = ?";
        return jdbc.queryForMap(query, id);
    }
    // END
}
