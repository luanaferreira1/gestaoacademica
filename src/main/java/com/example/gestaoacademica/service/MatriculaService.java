package com.example.gestaoacademica.service;

import com.example.gestaoacademica.dto.AtribuirNotaDTO;
import com.example.gestaoacademica.exception.ResourceNotFoundException;
import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.model.Disciplina;
import com.example.gestaoacademica.model.Matricula;
import com.example.gestaoacademica.repository.AlunoRepository;
import com.example.gestaoacademica.repository.DisciplinaRepository;
import com.example.gestaoacademica.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatriculaService {

    @Autowired private MatriculaRepository matriculaRepository;
    @Autowired private AlunoRepository alunoRepository;
    @Autowired private DisciplinaRepository disciplinaRepository;

    public Matricula matricular(Long alunoId, Long disciplinaId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + alunoId));
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID: " + disciplinaId));

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);

        return matriculaRepository.save(matricula);
    }

    public Matricula atribuirNota(AtribuirNotaDTO dto) {
        Matricula matricula = matriculaRepository.findByAlunoIdAndDisciplinaId(dto.getAlunoId(), dto.getDisciplinaId())
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula não encontrada para o aluno " + dto.getAlunoId() + " na disciplina " + dto.getDisciplinaId()));

        matricula.setNota(dto.getNota());
        return matriculaRepository.save(matricula);
    }

    public List<Aluno> findAprovadosByDisciplina(Long disciplinaId) {
        if (!disciplinaRepository.existsById(disciplinaId)) {
            throw new ResourceNotFoundException("Disciplina não encontrada com o ID: " + disciplinaId);
        }
        return matriculaRepository.findAlunosAprovados(disciplinaId);
    }

    public List<Aluno> findReprovadosByDisciplina(Long disciplinaId) {
        if (!disciplinaRepository.existsById(disciplinaId)) {
            throw new ResourceNotFoundException("Disciplina não encontrada com o ID: " + disciplinaId);
        }
        return matriculaRepository.findAlunosReprovados(disciplinaId);
    }
}