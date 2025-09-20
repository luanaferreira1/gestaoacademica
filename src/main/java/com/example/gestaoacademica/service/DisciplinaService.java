package com.example.gestaoacademica.service;

import com.example.gestaoacademica.dto.DisciplinaDTO;
import com.example.gestaoacademica.exception.ResourceNotFoundException;
import com.example.gestaoacademica.model.Disciplina;
import com.example.gestaoacademica.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public Disciplina create(DisciplinaDTO disciplinaDTO) {
        Disciplina disciplina = new Disciplina();
        disciplina.setNome(disciplinaDTO.getNome());
        disciplina.setCodigo(disciplinaDTO.getCodigo());
        return disciplinaRepository.save(disciplina);
    }

    public List<Disciplina> findAll() {
        return disciplinaRepository.findAll();
    }

    public Disciplina findById(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID: " + id));
    }
}