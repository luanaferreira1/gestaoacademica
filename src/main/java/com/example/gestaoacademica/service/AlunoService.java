package com.example.gestaoacademica.service;

import com.example.gestaoacademica.dto.AlunoDTO;
import com.example.gestaoacademica.exception.ResourceNotFoundException;
import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Aluno create(AlunoDTO alunoDTO) {
        Aluno aluno = new Aluno();
        aluno.setNome(alunoDTO.getNome());
        aluno.setCpf(alunoDTO.getCpf());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setTelefone(alunoDTO.getTelefone());
        aluno.setEndereco(alunoDTO.getEndereco());
        return alunoRepository.save(aluno);
    }

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Aluno findById(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com o ID: " + id));
    }
}