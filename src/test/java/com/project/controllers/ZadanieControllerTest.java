package com.project.controllers;

import com.project.model.Zadanie;
import com.project.services.ZadanieServiceImpl;
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
@WebMvcTest(controllers = ProjektController.class)
class ZadanieControllerTest {

    @MockBean
    private ZadanieServiceImpl zadanieService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllZadania() throws Exception {
        Zadanie zadanie = new Zadanie();
        zadanie.setZadanieId(1);
        zadanie.setNazwa("abc");
        List<Zadanie> list = new ArrayList<>();
        list.add(zadanie);
        list.add(zadanie);
        list.add(zadanie);

        when(zadanieService.getZadanies(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/zadanie")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].zadanieId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].zadanieId").exists());
    }

    @Test
    public void getZadanieById() throws Exception {
        Zadanie zadanie = new Zadanie();
        zadanie.setZadanieId(1);
        zadanie.setNazwa("abc");

        when(zadanieService.getZadanieById(1)).thenReturn(Optional.of(zadanie));
        mvc.perform(MockMvcRequestBuilders.get("/api/zadanie/1")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.zadanieId").value(1));
    }

    @Test
    public void getZadaniaProjektuById() throws Exception {
        Zadanie zadanie = new Zadanie();
        zadanie.setZadanieId(1);
        zadanie.setNazwa("abc");
        List<Zadanie> list = new ArrayList<>();
        list.add(zadanie);
        when(zadanieService.getZadaniaProjektu(1, 0, 20))
                .thenReturn(new PageImpl<>(list));
        mvc.perform(MockMvcRequestBuilders.get("/api/zadanie/1")
                        .with(user("user").password("password"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].projektId").value(1));
    }

    @Test
    public void deleteZadaniebyId() throws Exception {
        Zadanie zadanie = new Zadanie();
        zadanie.setZadanieId(1);
        zadanie.setNazwa("abc");

        when(zadanieService.getZadanieById(1)).thenReturn(Optional.of(zadanie));
        mvc.perform(MockMvcRequestBuilders.delete("/api/zadanie/1")
                        .with(user("user").password("password").roles("USER"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}

