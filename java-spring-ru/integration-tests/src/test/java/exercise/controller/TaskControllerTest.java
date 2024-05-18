package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    // BEGIN
    //Просмотр конкретной задачи
    @Test
    public void testShowTask() throws Exception {
        var task = taskRepository.save(generateTask());

        var result = mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertThatJson(result.getResponse().getContentAsString()).and(
                a -> a.node("title").isEqualTo(task.getTitle()),
                a -> a.node("description").isEqualTo(task.getDescription()),
                a -> a.node("createdAt").isEqualTo(task.getCreatedAt().toString()),
                a -> a.node("updatedAt").isEqualTo(task.getUpdatedAt().toString()));
    }

    //Создание новой задачи
    @Test
    public void testCreateTask() throws Exception {
        var tasksCount = taskRepository.findAll().size();
        var task = generateTask();

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(task));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(taskRepository.findAll()).hasSize(tasksCount + 1);

        var responseTask = om.readValue(result.getResponse().getContentAsString(), Task.class);
        assertTask(responseTask, task.getTitle(), task.getDescription());


        var actualTask = taskRepository.findById(responseTask.getId());
        assertThat(actualTask).isNotEmpty();
        assertTask(actualTask.get(), task.getTitle(), task.getDescription());
    }

    //Обновление существующей задачи
    @Test
    public void testUpdateTask() throws Exception {
        var task = taskRepository.save(generateTask());
        var tasksCount = taskRepository.findAll().size();
        var updatedTask = generateTask();

        var request = put("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(updatedTask));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        assertThat(taskRepository.findAll()).hasSize(tasksCount);

        var responseTask = om.readValue(result.getResponse().getContentAsString(), Task.class);
        assertTask(responseTask, updatedTask.getTitle(), updatedTask.getDescription(), task.getCreatedAt());

        var actualTask = taskRepository.findById(responseTask.getId());
        assertThat(actualTask).isNotEmpty();
        assertTask(actualTask.get(), updatedTask.getTitle(), updatedTask.getDescription(), task.getCreatedAt());
    }

    //Удаление задачи
    @Test
    public void testDeleteTask() throws Exception {
        var task = taskRepository.save(generateTask());
        var tasksCount = taskRepository.findAll().size();

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(taskRepository.findAll()).hasSize(tasksCount - 1);
        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }

    private Task generateTask() {
        var task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().maxLengthSentence(10))
                .supply(Select.field(Task::getCreatedAt), () -> generateLocalDate())
                .supply(Select.field(Task::getUpdatedAt), () -> generateLocalDate())
                .create();

        return task;
    }

    private LocalDate generateLocalDate() {
        return faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static void assertTask(Task actualTask, String title, String description) {
        assertTask(actualTask, title, description, LocalDate.now(), LocalDate.now());
    }

    private static void assertTask(Task actualTask, String title, String description, LocalDate createdAt) {
        assertTask(actualTask, title, description, createdAt, LocalDate.now());
    }

    private static void assertTask(Task actualTask, String title, String description, LocalDate createdAt, LocalDate updatedAt) {
        assertThat(actualTask.getTitle()).isEqualTo(title);
        assertThat(actualTask.getDescription()).isEqualTo(description);
        assertThat(actualTask.getCreatedAt()).isEqualTo(createdAt);
        assertThat(actualTask.getUpdatedAt()).isEqualTo(updatedAt);
    }
    // END
}


