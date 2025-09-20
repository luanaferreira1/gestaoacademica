package com.example.gestaoacademica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfessorRegistroDTO {
    @NotBlank
    private String nome;
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;
}