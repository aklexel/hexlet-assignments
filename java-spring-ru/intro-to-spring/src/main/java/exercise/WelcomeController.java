package exercise;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// BEGIN
@RestController
// Класс контроллера
public class WelcomeController {

    // Сопоставляем GET запрос на адрес /users с обработчиком
    @GetMapping("/")
    // Обработчик
    // Привязываем параметр запроса к параметру метода
    // и задаём значение по умолчанию
    public String welcome(@RequestParam(value = "name", defaultValue = "anonimus") String name) {
        return String.format("Welcome to Spring");
    }

    // Сопоставляем GET запрос на адрес /users с обработчиком
    @GetMapping("/users")
    // Обработчик
    // Привязываем параметр запроса к параметру метода
    // и задаём значение по умолчанию
    public String welcomeUser(@RequestParam(value = "name", defaultValue = "anonimus") String name) {
        return String.format("Welcome, %s!", name);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }
}
// END
