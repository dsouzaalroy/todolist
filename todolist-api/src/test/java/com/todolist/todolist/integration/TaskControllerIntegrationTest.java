package com.todolist.todolist.integration;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.todolist.dto.State;
import com.todolist.todolist.dto.Task;
import com.todolist.todolist.repository.TaskRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskControllerIntegrationTest {
    public static final String DEFAULT_ERROR_MSG = "Please inspect the request as it was invalid";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;

    private Task task;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
        objectMapper = Jackson2ObjectMapperBuilder
                .json().featuresToEnable(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES).build();
    }

    @Test
    public void getTasksShouldReturnNoTasks() throws Exception {
        // No existing tasks
        // given, when
        MvcResult mvcResult = mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andReturn();
        // then
        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertThat(jsonArray.length()).isZero();
    }

    @Test
    public void getTasksShouldReturnOneTask() throws Exception {
        // add one task
        // given
        addSampleData();
        // when
        MvcResult mvcResult = mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andReturn();
        // then
        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertThat(jsonArray.length()).isEqualTo(1);
        final JSONObject actualTask = jsonArray.getJSONObject(0);
        assertAll(() -> {
            assertThat(actualTask.getString("title")).isEqualTo(task.getTitle());
            assertThat(actualTask.getString("description")).isEqualTo(task.getDescription());
            assertThat(actualTask.getString("state")).isEqualTo(task.getState().toString());
            assertThat(actualTask.getString("dueDate")).isEqualTo(task.getDueDate().toString());
        });
    }

    @Test
    public void getTasksShouldReturnMultipleTasks() throws Exception {
        // add more than one task
        // given
        addSampleData();
        addSampleData("This is the second task");
        // when
        MvcResult mvcResult = mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andReturn();
        // then
        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertThat(jsonArray.length()).isEqualTo(2);
        final JSONObject actualTask1 = jsonArray.getJSONObject(0);
        final JSONObject actualTask2 = jsonArray.getJSONObject(1);
        assertAll(() -> {
            assertThat(actualTask1.getString("id")).isEqualTo("1");
            assertThat(actualTask2.getString("id")).isEqualTo("2");

            assertThat(actualTask1.getString("description")).isEqualTo("Description");
            assertThat(actualTask2.getString("description")).isEqualTo("This is the second task");
        });
    }

    @Test
    public void getTaskShouldReturnCorrectTaskFromId() throws Exception {
        // given
        addSampleData("I am one");
        addSampleData("I am two");
        addSampleData("I am three");
        // when
        MvcResult mvcResult1 = mockMvc.perform(get("/tasks/1")).andExpect(status().isOk()).andReturn();
        MvcResult mvcResult3 = mockMvc.perform(get("/tasks/3")).andExpect(status().isOk()).andReturn();
        // then
        final JSONObject actualTask1 = new JSONObject(mvcResult1.getResponse().getContentAsString());
        final JSONObject actualTask3 = new JSONObject(mvcResult3.getResponse().getContentAsString());
        assertAll(() -> {
            assertThat(actualTask1.getInt("id")).isEqualTo(1);
            assertThat(actualTask1.getString("description")).isEqualTo("I am one");
            assertThat(actualTask3.getInt("id")).isEqualTo(3);
            assertThat(actualTask3.getString("description")).isEqualTo("I am three");
        });
    }

    @Test
    public void getTaskShouldReturn4XXIfIdMissing() throws Exception {
       // given, when
        MvcResult mvcResult = mockMvc.perform(get("/tasks/1")).andExpect(status().isBadRequest()).andReturn();
        final String errorMsg = mvcResult.getResponse().getContentAsString();
        assertThat(errorMsg).isEqualTo(DEFAULT_ERROR_MSG);
    }

    @Test
    public void shouldUpdateTaskValues() throws Exception {
        // given
        addSampleData("I am the old description");
        Task tempRequestTask = new Task();
        tempRequestTask.setId(1L);
        tempRequestTask.setDescription("I am the new description");
        tempRequestTask.setState(State.DONE);
        // when
        MvcResult mvcResult = mockMvc.perform(
                put("/tasks/update")
                        .content(objectMapper.writeValueAsString(tempRequestTask))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        final JSONObject actualTask = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertAll(() -> {
            assertThat(actualTask.getString("description")).isEqualTo("I am the new description");
            assertThat(actualTask.getString("state")).isEqualTo("COMPLETED");
        });
    }

    @Test
    public void updateTaskReturns4XXIfRequestIsInvalid() throws Exception {
        // given
        addSampleData("I am the old description");
        Task tempRequestTask = new Task();
        tempRequestTask.setId(1L);
        tempRequestTask.setDescription("I am the new description");
        tempRequestTask.setState(State.DONE);
        String jsonRequest = objectMapper.writeValueAsString(tempRequestTask);
        jsonRequest = jsonRequest.replaceAll("COMPLETED", "FINISHED");
        // when
        mockMvc.perform(
                put("/tasks/update")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void shouldCreateTask() throws Exception {
        // given
        Task tempRequestTask = new Task();
        tempRequestTask.setTitle("Sample Title");
        tempRequestTask.setDescription("Sample Description");
        tempRequestTask.setDueDate(LocalDate.of(2025, 9, 14));
        // when
        MvcResult mvcResult = mockMvc.perform(
                        post("/tasks/create")
                                .content(objectMapper.writeValueAsString(tempRequestTask))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        // then
        final JSONObject actualTask = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertAll(() -> {
            // assert id and state are correctly defined
            assertThat(actualTask.getInt("id")).isEqualTo(1);
            assertThat(actualTask.getString("state")).isEqualTo("READY");
            assertThat(actualTask.getString("title")).isEqualTo(tempRequestTask.getTitle());
            assertThat(actualTask.getString("description")).isEqualTo(tempRequestTask.getDescription());
            assertThat(actualTask.getString("dueDate")).isEqualTo(tempRequestTask.getDueDate().toString());
        });
    }

    @Test
    public void createTaskShouldReturn4XXIfRequestIsInvalid() throws Exception {
        // given
        Task tempRequestTask = new Task();
        tempRequestTask.setTitle("Sample Title");
        tempRequestTask.setDescription("Sample Description");
        tempRequestTask.setDueDate(LocalDate.of(2025, 9, 14));
        String jsonRequest = objectMapper.writeValueAsString(task);
        jsonRequest = jsonRequest.replaceAll("2025-09-14", "Obviously-not-a-date");
        // when, then
        mockMvc.perform(
                        post("/tasks/create")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn();
    }

    private void addSampleData() {
        addSampleData("Description");
    }

    private void addSampleData(String desc) {
        task = new Task();
        task.setTitle("Title");
        task.setDescription(desc);
        task.setDueDate(LocalDate.of(2025, 9, 13));
        task.setState(State.TODO);
        taskRepository.save(task);
    }
}
