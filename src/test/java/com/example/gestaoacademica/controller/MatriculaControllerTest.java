package com.example.gestaoacademica.controller;

import com.example.gestaoacademica.config.SecurityConfig;
import com.example.gestaoacademica.dto.AtribuirNotaDTO;
import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.model.Matricula;
import com.example.gestaoacademica.service.MatriculaService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatriculaController.class)
@Import(SecurityConfig.class)
class MatriculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatriculaService matriculaService;

    @MockBean
    private ProfessorUserDetailsService professorUserDetailsService;

    @Test
    @WithMockUser
    void quandoMatricular_deveRetornarCreated() throws Exception {
        Matricula matricula = new Matricula();
        matricula.setId(1L);

        when(matriculaService.matricular(1L, 1L)).thenReturn(matricula);

        mockMvc.perform(post("/api/matriculas")
                        .param("alunoId", "1")
                        .param("disciplinaId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser
    void quandoAtribuirNota_deveRetornarOk() throws Exception {
        AtribuirNotaDTO dto = new AtribuirNotaDTO();
        dto.setAlunoId(1L);
        dto.setDisciplinaId(1L);
        dto.setNota(8.5);

        Matricula matricula = new Matricula();
        matricula.setId(1L);
        matricula.setNota(8.5);

        when(matriculaService.atribuirNota(any(AtribuirNotaDTO.class))).thenReturn(matricula);

        mockMvc.perform(put("/api/matriculas/notas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota").value(8.5));
    }

    @Test
    @WithMockUser
    void quandoListarAprovados_deveRetornarListaDeAlunos() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Aluno Aprovado");

        when(matriculaService.findAprovadosByDisciplina(1L)).thenReturn(List.of(aluno));

        mockMvc.perform(get("/api/matriculas/disciplinas/1/aprovados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Aluno Aprovado"));
    }

    @Test
    @WithMockUser
    void quandoListarReprovados_deveRetornarListaDeAlunos() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(2L);
        aluno.setNome("Aluno Reprovado");

        when(matriculaService.findReprovadosByDisciplina(1L)).thenReturn(List.of(aluno));

        mockMvc.perform(get("/api/matriculas/disciplinas/1/reprovados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Aluno Reprovado"));
    }
}
