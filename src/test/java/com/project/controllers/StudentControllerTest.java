package com.project.controllers;

import com.project.model.Project;
import com.project.model.Student;
import com.project.services.ProjectServiceImpl;
import com.project.services.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest {
    @MockBean
    private StudentService studentService;;
    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllStudents() throws Exception {
        Student student = new Student();
        student.setStudentId(1);
        student.setsName("abc");
        student.setName("abc");
        List<Student> list = new ArrayList<>();
        list.add(student);
        list.add(student);
        list.add(student);

        when(studentService.getStudents(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/student")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studentId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].studentId").exists());
    }
    @Test
    public void getStudentById() throws Exception {
        Student student = new Student();
        student.setStudentId(1);
        student.setsName("abc");
        student.setName("abc");

        when(studentService.getStudentById(1)).thenReturn(Optional.of(student));
        mvc.perform(MockMvcRequestBuilders.get("/api/student/1")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.studentId").value(1));
    }

    @Test
    public void getStudentBySName() throws Exception {
        Student student = new Student();
        student.setStudentId(1);
        student.setsName("abc");
        student.setName("abc");
        List<Student> list = new ArrayList<>();
        list.add(student);
        when(studentService.getStudentByNameCaseIgnore("abc", PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/student?nazwisko=abc")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].studentId").value(1));
    }

    @Test
    public void deleteStudentById() throws Exception {
        Student student = new Student();
        student.setStudentId(1);
        student.setsName("abc");
        student.setName("abc");

        when(studentService.getStudentById(1)).thenReturn(Optional.of(student));
        mvc.perform(MockMvcRequestBuilders.delete("/api/student/1")
                        .with(user("user").password("password").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }






}
