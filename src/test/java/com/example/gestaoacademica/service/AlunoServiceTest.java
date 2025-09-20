package com.example.gestaoacademica.service;

import com.example.gestaoacademica.dto.AlunoDTO;
import com.example.gestaoacademica.exception.ResourceNotFoundException;
import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.repository.AlunoRepository;
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
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    @Test
    @DisplayName("Deve criar um aluno com sucesso")
    void deveCriarAlunoComSucesso() {
        AlunoDTO alunoDTO = new AlunoDTO();
        alunoDTO.setNome("João da Silva");
        alunoDTO.setCpf("123.456.789-00");
        alunoDTO.setEmail("joao.silva@email.com");

        Aluno alunoSalvo = new Aluno();
        alunoSalvo.setId(1L);
        alunoSalvo.setNome("João da Silva");

        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoSalvo);

        Aluno resultado = alunoService.create(alunoDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João da Silva", resultado.getNome());
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }

    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosOsAlunos() {
        Aluno aluno1 = new Aluno();
        aluno1.setId(1L);
        aluno1.setNome("Aluno Um");

        Aluno aluno2 = new Aluno();
        aluno2.setId(2L);
        aluno2.setNome("Aluno Dois");

        List<Aluno> listaDeAlunos = List.of(aluno1, aluno2);

        when(alunoRepository.findAll()).thenReturn(listaDeAlunos);

        List<Aluno> resultado = alunoService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(alunoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve encontrar um aluno pelo ID com sucesso")
    void deveEncontrarAlunoPeloIdComSucesso() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        Aluno resultado = alunoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(alunoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando aluno não for encontrado pelo ID")
    void deveLancarExcecaoQuandoAlunoNaoForEncontradoPeloId() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            alunoService.findById(99L);
        });

        verify(alunoRepository, times(1)).findById(99L);
    }
}