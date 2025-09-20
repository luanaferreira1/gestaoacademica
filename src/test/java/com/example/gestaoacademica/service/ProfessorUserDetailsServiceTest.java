package com.example.gestaoacademica.service;

import com.example.gestaoacademica.model.Professor;
import com.example.gestaoacademica.repository.ProfessorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfessorUserDetailsServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorUserDetailsService userDetailsService;

    @Test
    @DisplayName("Deve carregar usuário por username com sucesso")
    void deveCarregarUsuarioPorUsernameComSucesso() {
        Professor professor = new Professor();
        professor.setUsername("carlos.prof");
        professor.setPassword("senhaCriptografada");

        when(professorRepository.findByUsername("carlos.prof")).thenReturn(Optional.of(professor));

        UserDetails userDetails = userDetailsService.loadUserByUsername("carlos.prof");

        assertNotNull(userDetails);
        assertEquals("carlos.prof", userDetails.getUsername());
        assertEquals("senhaCriptografada", userDetails.getPassword());
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando professor não existe")
    void deveLancarExcecaoQuandoProfessorNaoExiste() {
        when(professorRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("inexistente");
        });
    }
}