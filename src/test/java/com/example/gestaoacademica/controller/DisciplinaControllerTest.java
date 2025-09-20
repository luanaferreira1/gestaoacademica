package com.example.gestaoacademica.controller;

import com.example.gestaoacademica.exception.ResourceNotFoundException;
import com.example.gestaoacademica.config.SecurityConfig;
import com.example.gestaoacademica.dto.DisciplinaDTO;
import com.example.gestaoacademica.model.Disciplina;
import com.example.gestaoacademica.service.DisciplinaService;
import com.example.gestaoacademica.service.ProfessorUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DisciplinaController.class)
@Import(SecurityConfig.class)
class DisciplinaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DisciplinaService disciplinaService;

    @MockBean
    private ProfessorUserDetailsService professorUserDetailsService;

    @Test
    @WithMockUser
    void quandoCriarDisciplina_deveRetornarCreated() throws Exception {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setNome("Cálculo I");
        dto.setCodigo("MAT101");

        Disciplina disciplinaSalva = new Disciplina();
        disciplinaSalva.setId(1L);
        disciplinaSalva.setNome("Cálculo I");

        when(disciplinaService.create(any(DisciplinaDTO.class))).thenReturn(disciplinaSalva);

        mockMvc.perform(post("/api/disciplinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Cálculo I"));
    }

    @Test
    @WithMockUser
    void quandoListarTodas_deveRetornarListaDeDisciplinas() throws Exception {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Cálculo I");

        when(disciplinaService.findAll()).thenReturn(List.of(disciplina));

        mockMvc.perform(get("/api/disciplinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Cálculo I"));
    }

    @Test
    @WithMockUser
    void quandoBuscarPorIdExistente_deveRetornarDisciplina() throws Exception {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Cálculo I");

        when(disciplinaService.findById(1L)).thenReturn(disciplina);

        mockMvc.perform(get("/api/disciplinas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Cálculo I"));
    }

    @Test
    @WithMockUser
    void quandoBuscarPorIdInexistente_deveRetornarNotFound() throws Exception {
        when(disciplinaService.findById(99L)).thenThrow(new ResourceNotFoundException("Disciplina não encontrada"));

        mockMvc.perform(get("/api/disciplinas/99"))
                .andExpect(status().isNotFound());
    }
}
