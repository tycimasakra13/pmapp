package com.project.controllers;

import com.project.model.Project;
import com.project.services.ProjectServiceImpl;
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
class ProjectControllerTest {

    @MockBean
    private ProjectServiceImpl projectService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllProjekts() throws Exception {
        Project project = new Project();
        project.setProjectId(1);
        project.setName("abc");
        List<Project> list = new ArrayList<>();
        list.add(project);
        list.add(project);
        list.add(project);

        when(projectService.getProjects(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/projekt")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].projektId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].projektId").exists());
    }

    @Test
    public void getProjectById() throws Exception {
        Project project = new Project();
        project.setProjectId(1);
        project.setName("abc");

        when(projectService.getProjectById(1)).thenReturn(Optional.of(project));
        mvc.perform(MockMvcRequestBuilders.get("/api/projekt/1")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projektId").value(1));
    }

    @Test
    public void getProjectByName() throws Exception {
        Project project = new Project();
        project.setProjectId(1);
        project.setName("abc");
        List<Project> list = new ArrayList<>();
        list.add(project);
        when(projectService.searchByName("ab", PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/projekt?nazwa=ab")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].projektId").value(1));
    }

    @Test
    public void deleteProjectById() throws Exception {
        Project project = new Project();
        project.setProjectId(1);
        project.setName("abc");

        when(projectService.getProjectById(1)).thenReturn(Optional.of(project));
        mvc.perform(MockMvcRequestBuilders.delete("/api/projekt/1")
                        .with(user("user").password("password").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}