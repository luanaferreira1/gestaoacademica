package com.example.gestaoacademica.controller;

import com.example.gestaoacademica.exception.ResourceNotFoundException;
import com.example.gestaoacademica.config.SecurityConfig;
import com.example.gestaoacademica.dto.AlunoDTO;
import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.service.AlunoService;
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

@WebMvcTest(AlunoController.class)
@Import(SecurityConfig.class)
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlunoService alunoService;

    @MockBean
    private ProfessorUserDetailsService professorUserDetailsService;


    @Test
    @WithMockUser
    void quandoCriarAluno_deveRetornarCreated() throws Exception {
        AlunoDTO dto = new AlunoDTO();
        dto.setNome("Ana");
        dto.setCpf("123");
        dto.setEmail("ana@email.com");

        Aluno alunoSalvo = new Aluno();
        alunoSalvo.setId(1L);
        alunoSalvo.setNome("Ana");

        when(alunoService.create(any(AlunoDTO.class))).thenReturn(alunoSalvo);

        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Ana"));
    }

    @Test
    @WithMockUser
    void quandoListarTodos_deveRetornarListaDeAlunos() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Ana");

        when(alunoService.findAll()).thenReturn(List.of(aluno));

        mockMvc.perform(get("/api/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Ana"));
    }

    @Test
    @WithMockUser
    void quandoBuscarPorIdInexistente_deveRetornarNotFound() throws Exception {
        when(alunoService.findById(99L)).thenThrow(new ResourceNotFoundException("Aluno não encontrado"));

        mockMvc.perform(get("/api/alunos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Aluno não encontrado"));
    }
}
