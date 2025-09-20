package com.example.gestaoacademica.controller;

import com.example.gestaoacademica.config.SecurityConfig;
import com.example.gestaoacademica.dto.ProfessorRegistroDTO;
import com.example.gestaoacademica.model.Professor;
import com.example.gestaoacademica.repository.ProfessorRepository;
import com.example.gestaoacademica.service.ProfessorUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ProfessorRepository professorRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ProfessorUserDetailsService professorUserDetailsService;


    @Test
    void quandoRegistrarComDadosValidos_deveRetornarCreated() throws Exception {
        ProfessorRegistroDTO dto = new ProfessorRegistroDTO();
        dto.setNome("Novo Prof");
        dto.setUsername("novo.prof");
        dto.setPassword("senha123");

        when(professorRepository.findByUsername("novo.prof")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        when(professorRepository.save(any(Professor.class))).thenReturn(new Professor());

        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void quandoRegistrarComUsernameExistente_deveRetornarBadRequest() throws Exception {
        ProfessorRegistroDTO dto = new ProfessorRegistroDTO();
        dto.setNome("Novo Prof");
        dto.setUsername("novo.prof");
        dto.setPassword("senha123");

        when(professorRepository.findByUsername("novo.prof")).thenReturn(Optional.of(new Professor()));

        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
