package com.example.gestaoacademica.controller;

import com.example.gestaoacademica.dto.LoginRequestDTO;
import com.example.gestaoacademica.dto.ProfessorRegistroDTO;
import com.example.gestaoacademica.model.Professor;
import com.example.gestaoacademica.repository.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        try {
            authenticationManager.authenticate(usernamePassword);
            return ResponseEntity.ok("Login bem-sucedido. As próximas requisições estarão autenticadas.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody @Valid ProfessorRegistroDTO data) {
        if (this.professorRepository.findByUsername(data.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = passwordEncoder.encode(data.getPassword());
        Professor novoProfessor = new Professor();
        novoProfessor.setNome(data.getNome());
        novoProfessor.setUsername(data.getUsername());
        novoProfessor.setPassword(encryptedPassword);

        this.professorRepository.save(novoProfessor);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}