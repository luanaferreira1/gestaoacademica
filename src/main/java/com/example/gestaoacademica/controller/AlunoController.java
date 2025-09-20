package com.example.gestaoacademica.controller;

import com.example.gestaoacademica.dto.AlunoDTO;
import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity<Aluno> create(@Valid @RequestBody AlunoDTO alunoDTO) {
        Aluno novoAluno = alunoService.create(alunoDTO);
        return new ResponseEntity<>(novoAluno, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> getAll() {
        List<Aluno> alunos = alunoService.findAll();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getById(@PathVariable Long id) {
        Aluno aluno = alunoService.findById(id);
        return ResponseEntity.ok(aluno);
    }
}