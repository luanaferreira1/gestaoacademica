package com.example.gestaoacademica.service;

import com.example.gestaoacademica.dto.AtribuirNotaDTO;
import com.example.gestaoacademica.exception.ResourceNotFoundException;
import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.model.Disciplina;
import com.example.gestaoacademica.model.Matricula;
import com.example.gestaoacademica.repository.AlunoRepository;
import com.example.gestaoacademica.repository.DisciplinaRepository;
import com.example.gestaoacademica.repository.MatriculaRepository;
import org.junit.jupiter.api.BeforeEach;
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
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private MatriculaService matriculaService;

    private Aluno aluno;
    private Disciplina disciplina;
    private Matricula matricula;

    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Ana Silva");

        disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Cálculo I");

        matricula = new Matricula();
        matricula.setId(1L);
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);
    }

    @Test
    @DisplayName("Deve matricular aluno com sucesso")
    void deveMatricularAlunoComSucesso() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(disciplinaRepository.findById(1L)).thenReturn(Optional.of(disciplina));
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        Matricula resultado = matriculaService.matricular(1L, 1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ana Silva", resultado.getAluno().getNome());

        verify(alunoRepository, times(1)).findById(1L);
        verify(disciplinaRepository, times(1)).findById(1L);
        verify(matriculaRepository, times(1)).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao matricular aluno inexistente")
    void deveLancarExcecaoAoMatricularAlunoInexistente() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            matriculaService.matricular(99L, 1L);
        });

        verify(matriculaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atribuir nota com sucesso")
    void deveAtribuirNotaComSucesso() {
        AtribuirNotaDTO notaDTO = new AtribuirNotaDTO();
        notaDTO.setAlunoId(1L);
        notaDTO.setDisciplinaId(1L);
        notaDTO.setNota(8.5);

        matricula.setNota(8.5);

        when(matriculaRepository.findByAlunoIdAndDisciplinaId(1L, 1L)).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        Matricula resultado = matriculaService.atribuirNota(notaDTO);

        assertNotNull(resultado);
        assertEquals(8.5, resultado.getNota());
        verify(matriculaRepository, times(1)).findByAlunoIdAndDisciplinaId(1L, 1L);
        verify(matriculaRepository, times(1)).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atribuir nota para matrícula inexistente")
    void deveLancarExcecaoAoAtribuirNotaParaMatriculaInexistente() {
        AtribuirNotaDTO notaDTO = new AtribuirNotaDTO();
        notaDTO.setAlunoId(99L);
        notaDTO.setDisciplinaId(99L);
        notaDTO.setNota(8.5);

        when(matriculaRepository.findByAlunoIdAndDisciplinaId(99L, 99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            matriculaService.atribuirNota(notaDTO);
        });

        verify(matriculaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar alunos aprovados")
    void deveListarAlunosAprovados() {
        when(disciplinaRepository.existsById(1L)).thenReturn(true);
        when(matriculaRepository.findAlunosAprovados(1L)).thenReturn(List.of(aluno));

        List<Aluno> aprovados = matriculaService.findAprovadosByDisciplina(1L);

        assertNotNull(aprovados);
        assertEquals(1, aprovados.size());
        assertEquals("Ana Silva", aprovados.get(0).getNome());
        verify(matriculaRepository, times(1)).findAlunosAprovados(1L);
    }

    @Test
    @DisplayName("Deve retornar lista vazia de reprovados")
    void deveRetornarListaVaziaDeReprovados() {
        when(disciplinaRepository.existsById(1L)).thenReturn(true);
        when(matriculaRepository.findAlunosReprovados(1L)).thenReturn(List.of());

        List<Aluno> reprovados = matriculaService.findReprovadosByDisciplina(1L);

        assertNotNull(reprovados);
        assertTrue(reprovados.isEmpty());
        verify(matriculaRepository, times(1)).findAlunosReprovados(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar aprovados de disciplina inexistente")
    void deveLancarExcecaoAoBuscarAprovadosDeDisciplinaInexistente() {
        when(disciplinaRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            matriculaService.findAprovadosByDisciplina(99L);
        });

        verify(matriculaRepository, never()).findAlunosAprovados(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar reprovados de disciplina inexistente")
    void deveLancarExcecaoAoBuscarReprovadosDeDisciplinaInexistente() {
        when(disciplinaRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            matriculaService.findReprovadosByDisciplina(99L);
        });

        verify(matriculaRepository, never()).findAlunosReprovados(anyLong());
    }
}