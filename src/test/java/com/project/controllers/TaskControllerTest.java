package com.project.controllers;

import com.project.model.Project;
import com.project.model.Task;
import com.project.services.ProjectServiceImpl;
import com.project.services.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProjectController.class)
class TaskControllerTest {

    @MockBean
    private TaskServiceImpl taskService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllTasks() throws Exception {
        Task task = new Task();
        task.setTaskId(1);
        task.setName("abc");
        List<Task> list = new ArrayList<>();
        list.add(task);
        list.add(task);
        list.add(task);

        when(taskService.getTasks(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/zadanie")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].zadanieId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].zadanieId").exists());
    }

    @Test
    public void getTaskById() throws Exception {
        Task task = new Task();
        task.setTaskId(1);
        task.setName("abc");

        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));
        mvc.perform(MockMvcRequestBuilders.get("/api/zadanie/1")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.zadanieId").value(1));
    }

    @Test
    public void getProjectTasksById() throws Exception {
        Task task = new Task();
        task.setTaskId(1);
        task.setName("abc");
        List<Task> list = new ArrayList<>();
        list.add(task);
        when(taskService.getProjectTasks(1, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/zadanie/1")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].projektId").value(1));
    }

    @Test
    public void deleteTaskById() throws Exception {
        Task task = new Task();
        task.setTaskId(1);
        task.setName("abc");

        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));
        mvc.perform(MockMvcRequestBuilders.delete("/api/zadanie/1")
                        .with(user("user").password("password").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}