package com.example.gestaoacademica.service;

import com.example.gestaoacademica.dto.DisciplinaDTO;
import com.example.gestaoacademica.exception.ResourceNotFoundException;
import com.example.gestaoacademica.model.Disciplina;
import com.example.gestaoacademica.repository.DisciplinaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private DisciplinaService disciplinaService;

    @Test
    @DisplayName("Deve criar uma disciplina com sucesso")
    void deveCriarDisciplinaComSucesso() {
        DisciplinaDTO disciplinaDTO = new DisciplinaDTO();
        disciplinaDTO.setNome("Cálculo I");
        disciplinaDTO.setCodigo("MAT101");

        Disciplina disciplinaSalva = new Disciplina();
        disciplinaSalva.setId(1L);
        disciplinaSalva.setNome("Cálculo I");

        when(disciplinaRepository.save(any(Disciplina.class))).thenReturn(disciplinaSalva);

        Disciplina resultado = disciplinaService.create(disciplinaDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Cálculo I", resultado.getNome());
        verify(disciplinaRepository, times(1)).save(any(Disciplina.class));
    }

    @Test
    @DisplayName("Deve listar todas as disciplinas")
    void deveListarTodasAsDisciplinas() {
        Disciplina disciplina1 = new Disciplina();
        disciplina1.setId(1L);
        disciplina1.setNome("Cálculo I");

        Disciplina disciplina2 = new Disciplina();
        disciplina2.setId(2L);
        disciplina2.setNome("Física I");

        List<Disciplina> listaDeDisciplinas = List.of(disciplina1, disciplina2);

        when(disciplinaRepository.findAll()).thenReturn(listaDeDisciplinas);

        List<Disciplina> resultado = disciplinaService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(disciplinaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve encontrar uma disciplina pelo ID com sucesso")
    void deveEncontrarDisciplinaPeloIdComSucesso() {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1L);
        when(disciplinaRepository.findById(1L)).thenReturn(Optional.of(disciplina));

        Disciplina resultado = disciplinaService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(disciplinaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando disciplina não for encontrada pelo ID")
    void deveLancarExcecaoQuandoDisciplinaNaoForEncontradaPeloId() {
        when(disciplinaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            disciplinaService.findById(99L);
        });

        verify(disciplinaRepository, times(1)).findById(99L);
    }
}