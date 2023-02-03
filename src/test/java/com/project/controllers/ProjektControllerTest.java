package com.project.controllers;

import com.project.model.Projekt;
import com.project.services.ProjektServiceImpl;
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
@WebMvcTest(controllers = ProjektController.class)
class ProjektControllerTest {

    @MockBean
    private ProjektServiceImpl projektService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllProjekts() throws Exception {
        Projekt projekt = new Projekt();
        projekt.setProjektId(1);
        projekt.setNazwa("abc");
        List<Projekt> list = new ArrayList<>();
        list.add(projekt);
        list.add(projekt);
        list.add(projekt);

        when(projektService.getProjekts(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/projekt")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].projektId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].projektId").exists());
    }

    @Test
    public void getProjektById() throws Exception {
        Projekt projekt = new Projekt();
        projekt.setProjektId(1);
        projekt.setNazwa("abc");

        when(projektService.getProjektById(1)).thenReturn(Optional.of(projekt));
        mvc.perform(MockMvcRequestBuilders.get("/api/projekt/1")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projektId").value(1));
    }

    @Test
    public void getProjektByName() throws Exception {
        Projekt projekt = new Projekt();
        projekt.setProjektId(1);
        projekt.setNazwa("abc");
        List<Projekt> list = new ArrayList<>();
        list.add(projekt);
        when(projektService.searchByNazwa("ab", 0, 20))
                .thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/projekt?nazwa=ab")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].projektId").value(1));
    }

    @Test
    public void deleteProjektbyId() throws Exception {
        Projekt projekt = new Projekt();
        projekt.setProjektId(1);
        projekt.setNazwa("abc");

        when(projektService.getProjektById(1)).thenReturn(Optional.of(projekt));
        mvc.perform(MockMvcRequestBuilders.delete("/api/projekt/1")
                        .with(user("user").password("password").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}