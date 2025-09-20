package com.example.gestaoacademica.controller;

import com.example.gestaoacademica.dto.AtribuirNotaDTO;
import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.model.Matricula;
import com.example.gestaoacademica.service.MatriculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<Matricula> matricular(@RequestParam Long alunoId, @RequestParam Long disciplinaId) {
        Matricula novaMatricula = matriculaService.matricular(alunoId, disciplinaId);
        return new ResponseEntity<>(novaMatricula, HttpStatus.CREATED);
    }

    @PutMapping("/notas")
    public ResponseEntity<Matricula> atribuirNota(@Valid @RequestBody AtribuirNotaDTO dto) {
        Matricula matriculaAtualizada = matriculaService.atribuirNota(dto);
        return ResponseEntity.ok(matriculaAtualizada);
    }

    @GetMapping("/disciplinas/{disciplinaId}/aprovados")
    public ResponseEntity<List<Aluno>> getAprovados(@PathVariable Long disciplinaId) {
        List<Aluno> aprovados = matriculaService.findAprovadosByDisciplina(disciplinaId);
        return ResponseEntity.ok(aprovados);
    }

    @GetMapping("/disciplinas/{disciplinaId}/reprovados")
    public ResponseEntity<List<Aluno>> getReprovados(@PathVariable Long disciplinaId) {
        List<Aluno> reprovados = matriculaService.findReprovadosByDisciplina(disciplinaId);
        return ResponseEntity.ok(reprovados);
    }
}