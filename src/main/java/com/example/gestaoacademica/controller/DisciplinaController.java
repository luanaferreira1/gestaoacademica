package com.example.gestaoacademica.controller;

import com.example.gestaoacademica.dto.DisciplinaDTO;
import com.example.gestaoacademica.model.Disciplina;
import com.example.gestaoacademica.service.DisciplinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @PostMapping
    public ResponseEntity<Disciplina> create(@Valid @RequestBody DisciplinaDTO disciplinaDTO) {
        Disciplina novaDisciplina = disciplinaService.create(disciplinaDTO);
        return new ResponseEntity<>(novaDisciplina, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Disciplina>> getAll() {
        return ResponseEntity.ok(disciplinaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disciplina> getById(@PathVariable Long id) {
        return ResponseEntity.ok(disciplinaService.findById(id));
    }
}